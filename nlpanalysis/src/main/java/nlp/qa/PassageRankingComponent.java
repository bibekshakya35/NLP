package nlp.qa;

import lombok.extern.slf4j.Slf4j;
import nlp.lucene.index.TermVectorMapper;
import nlp.lucene.index.TermVectorOffsetInfo;
import nlp.solr.NameFilter;
import opennlp.tools.util.Span;
import org.apache.lucene.codecs.lucene50.Lucene50TermVectorsFormat;
import org.apache.lucene.index.*;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.*;
import org.apache.lucene.util.PriorityQueue;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.index.SlowCompositeReaderWrapper;
import org.apache.solr.search.DocList;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.plugin.PluginInfoInitialized;
import org.apache.solr.util.plugin.SolrCoreAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Given a SpanQuery, get windows around the matches and rank those results
 */
public class PassageRankingComponent extends SearchComponent implements PluginInfoInitialized, SolrCoreAware, QAParams {
    private transient static Logger log = LoggerFactory.getLogger(PassageRankingComponent.class);

    static final String NE_PREFIX_LOWER = NameFilter.NE_PREFIX.toLowerCase();

    public static final int DEFAULT_PRIMARY_WINDOW_SIZE = 25;
    public static final int DEFAULT_ADJACENT_WINDOW_SIZE = 25;
    public static final int DEFAULT_SECONDARY_WINDOW_SIZE = 25;

    public static final float DEFAULT_ADJACENT_WEIGHT = 0.5f;
    public static final float DEFAULT_SECOND_ADJACENT_WEIGHT = 0.25f;
    public static final float DEFAULT_BIGRAM_WEIGHT = 1.0f;

    @Override
    public void prepare(ResponseBuilder responseBuilder) throws IOException {
        SolrParams params = responseBuilder.req.getParams();
        if (!params.getBool(COMPONENT_NAME, false)) {
            return;
        }
    }

    @Override
    public void process(ResponseBuilder responseBuilder) throws IOException {
        SolrParams params = responseBuilder.req.getParams();
        if (!params.getBool(COMPONENT_NAME, false)) {
            return;
        }
        Query originalQuery = responseBuilder.getQuery();
        //TODO: longer term, we don't have to be a span query, we could re-analyze the document
        if (originalQuery != null) {
            if (!(originalQuery instanceof SpanNearQuery)) {
                throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Illegal query type. The incoming query must be a Lucene SpanNearQuery and it was a" + originalQuery
                        .getClass().getName());
            }
            SpanNearQuery spanNearQuery = (SpanNearQuery) originalQuery;

            SolrIndexSearcher searcher = responseBuilder.req.getSearcher();
            IndexReader reader = searcher.getIndexReader();
            SpanWeight spanWeight = spanNearQuery.createWeight(searcher, false, 1.0f);
            LeafReader pseudoAtomicReader = SlowCompositeReaderWrapper.wrap(reader);
            Spans spans = spanWeight.getSpans(pseudoAtomicReader.getContext(), SpanWeight.Postings.POSITIONS);
            //Assume a query is SpanQuery
            //build up the query term weight map and the bi-gram
            Map<String, Float> termWeights = new HashMap<>();
            Map<String, Float> bigramWeights = new HashMap<>();
            createWeights(params.get(CommonParams.Q),
                    spanNearQuery,
                    termWeights, bigramWeights, reader);
            float adjWeight = params.getFloat(ADJACENT_WEIGHT, DEFAULT_ADJACENT_WEIGHT);
            float secondAdjWeight = params.getFloat(SECOND_ADJ_WEIGHT, DEFAULT_SECOND_ADJACENT_WEIGHT);
            float bigramWeight = params.getFloat(BIGRAM_WEIGHT, DEFAULT_BIGRAM_WEIGHT);
            //get the passages
            int primaryWindowSize = params.getInt(QAParams.PRIMARY_WINDOW_SIZE, DEFAULT_PRIMARY_WINDOW_SIZE);
            int adjacentWindowSize = params.getInt(QAParams.ADJACENT_WINDOW_SIZE, DEFAULT_ADJACENT_WINDOW_SIZE);
            int secondaryWindowSize = params.getInt(QAParams.SECONDARY_WINDOW_SIZE, DEFAULT_SECONDARY_WINDOW_SIZE);
            WindowBuildingTVM tvm = new WindowBuildingTVM(primaryWindowSize, adjacentWindowSize, secondaryWindowSize);
            PassagePriorityQueue rankedPassages = new PassagePriorityQueue();
            DocList docList = responseBuilder.getResults().docList;
            while ((spans.nextDoc()) != spans.NO_MORE_POSITIONS) {
                //build up the window
                if (docList.exists(spans.docID())){
                    tvm.spanStart = spans.startPosition();
                    tvm.spanEnd = spans.endPosition();
                    Terms terms = reader.getTermVector(spans.docID(),spanNearQuery.getField());
                    if (terms!=null){

                    }
                }
            }


        }
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void init(PluginInfo pluginInfo) {

    }

    @Override
    public void inform(SolrCore solrCore) {

    }

    protected void createWeights(String originalQuery,
                                 SpanNearQuery parsedQuery,
                                 Map<String, Float> termWeight,
                                 Map<String, Float> bigramWeights,
                                 IndexReader reader) {
        SpanQuery[] clauses = parsedQuery.getClauses();
        //we need to recurse through the clauses until we get to SpanTermQuery
        Term lastTerm = null;
        Float lastWeight = null;
        for (int i = 0; i < clauses.length; i++) {
            SpanQuery clause = clauses[i];
            if (clause instanceof SpanTermQuery) {
                Term term = ((SpanTermQuery) clause).getTerm();
                try {
                    Float weight = calculateWeight(term, reader);
                    termWeight.put(term.text(), weight);
                    if (lastTerm != null) {
                        //calculate the bi-gram
                        //use the smaller of the two weight
                        if (lastWeight.floatValue() < weight.floatValue()) {
                            bigramWeights.put(lastTerm + "," + term.text(), new Float(lastWeight.floatValue() * 0.25));
                        } else {
                            bigramWeights.put(lastTerm + "," + term.text(), new Float(weight.floatValue() * 0.25));
                        }

                    }
                    //last
                    lastTerm = term;
                    lastWeight = weight;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Unhandled query type: " + clause.getClass().getName());
            }
        }
    }

    private float calculateWeight(Term term, IndexReader reader) throws IOException {
        //if a term is not in the index, then it's a weight is 0
        TermsEnum termsEnum = MultiFields.getTerms(reader, term.field())
                .iterator();
        if (termsEnum != null && termsEnum.term() != null && termsEnum.term().equals(term)) {
            return 1.0f / termsEnum.docFreq();
        } else {
            log.warn("Couldn't find doc freq for term {}", term);
            return 0;
        }
    }



    class PassagePriorityQueue extends PriorityQueue<Passage> {
        public PassagePriorityQueue() {
            super(10);
        }

        public PassagePriorityQueue(int maxSize) {
            super(maxSize);
        }

        @Override
        protected boolean lessThan(Passage passageA, Passage passageB) {
            if (passageA.score == passageB.score) return passageA.lDocId > passageB.lDocId;
            else return passageA.lDocId < passageB.lDocId;
        }
    }



}
