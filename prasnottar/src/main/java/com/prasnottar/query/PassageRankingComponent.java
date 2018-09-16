package com.prasnottar.query;

import com.prasnottar.solr.NameFilter;
import com.prasnottar.summarizer.Summarizer;
import edu.stanford.nlp.stats.Counter;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.*;
import org.apache.lucene.util.PriorityQueue;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.PluginInfo;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.component.ResponseBuilder;
import org.apache.solr.handler.component.SearchComponent;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.DocList;
import org.apache.solr.search.SolrIndexSearcher;
import org.apache.solr.util.plugin.PluginInfoInitialized;
import org.apache.solr.util.plugin.SolrCoreAware;

import java.io.IOException;
import java.util.*;

/**
 * Given a SpanQuery or BooleanQuery, get windows around the matches and rank those results
 */
@Slf4j
public class PassageRankingComponent extends SearchComponent implements PluginInfoInitialized, SolrCoreAware, PrasnottarParam {
    static final String NE_PREFIX_LOWER = NameFilter.NE_PREFIX.toLowerCase();
    public static final int DEFAULT_PRIMARY_WINDOW_SIZE = 25;
    public static final int DEFAULT_ADJACENT_WINDOW_SIZE = 25;
    public static final int DEFAULT_SECONDARY_WINDOW_SIZE = 25;

    public static final float DEFAULT_ADJACENT_WEIGHT = 0.5f;
    public static final float DEFAULT_SECOND_ADJACENT_WEIGHT = 0.25f;
    public static final float DEFAULT_BIGRAM_WEIGHT = 1.0f;
    private static final String DF_COUNTER_PATH = "df-counts.ser";

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
        SolrIndexSearcher searcher = responseBuilder.req.getSearcher();

        if (originalQuery != null) {
            if (originalQuery instanceof BooleanQuery) {
                BooleanQuery booleanQuery = (BooleanQuery) originalQuery;
                TopDocs hits = searcher.search(booleanQuery, params.getInt(OWL_ROWS, 5));
                int start = 0;
                int end = (int) Math.min(hits.totalHits, params.getInt(OWL_ROWS, 5));
                ScoreDoc[] results = hits.scoreDocs;
                NamedList qaResponse = new NamedList();
                responseBuilder.rsp.add("qaResponse", qaResponse);
                Counter<String> dfCounter = null;
                try {
                    dfCounter = Summarizer.loadDfCounter(DF_COUNTER_PATH);
                } catch (ClassNotFoundException e) {
                    log.error("File {} is not placed in server folder", DF_COUNTER_PATH);
                    e.printStackTrace();
                }
                Summarizer summarizer = new Summarizer(dfCounter);
                for (int i = start; i < end; i++) {
                    Document doc = searcher.doc(results[i].doc);
                    NamedList passNL = new NamedList();
                    qaResponse.add(("answer"), passNL);
                    passNL.add("luceneDocId", doc.get("id"));
                    passNL.add("window", summarizer.summarize(doc.get("content"), 5));
                }
            }
        }
        if (originalQuery instanceof SpanOrQuery) {
            SpanOrQuery spanOrQuery = (SpanOrQuery) originalQuery;
            IndexReader indexReader = searcher.getIndexReader();
            List<LeafReaderContext> ctxs = (List<LeafReaderContext>) indexReader.leaves();
            LeafReaderContext context = ctxs.get(0);
            SpanWeight spanWeight = spanOrQuery.createWeight(searcher, true, 1f);
            Spans spans = spanWeight.getSpans(context, SpanWeight.Postings.POSITIONS);
            //Assumes the query is a SpanQuery
            //Build up the query term weight map and the bi-gram
            Map<String, Float> termWeights = new HashMap<>();
            Map<String, Float> bigramWeights = new HashMap<>();
            createWeights(params.get(CommonParams.Q), spanOrQuery, termWeights, bigramWeights, indexReader);
            float adjWeight = params.getFloat(ADJACENT_WEIGHT, DEFAULT_ADJACENT_WEIGHT);
            float secondAdjWeight = params.getFloat(SECOND_ADJ_WEIGHT, DEFAULT_SECOND_ADJACENT_WEIGHT);
            float bigramWeight = params.getFloat(BIGRAM_WEIGHT, DEFAULT_BIGRAM_WEIGHT);
            //GET THE PASSAGE
            int primaryWindowSize = params.getInt(PRIMARY_WINDOW_SIZE, DEFAULT_PRIMARY_WINDOW_SIZE);
            int adjacentWindowSize = params.getInt(ADJACENT_WINDOW_SIZE, DEFAULT_ADJACENT_WINDOW_SIZE);
            int secondaryWindowSize = params.getInt(SECONDARY_WINDOW_SIZE, DEFAULT_SECONDARY_WINDOW_SIZE);

            WindowBuildingTVM tvm = new WindowBuildingTVM(primaryWindowSize, adjacentWindowSize, secondaryWindowSize);
            PassagePriorityQueue rankedPassages = new PassagePriorityQueue();
            //intersect w/ doclist
            DocList docList = responseBuilder.getResults().docList;
            while (spans != null && spans.nextDoc() != Spans.NO_MORE_DOCS) {
                //build up the window
                if (docList.exists(spans.docID())) {
                    spans.nextStartPosition();
                    String exp = spanWeight.explain(context, spans.docID()).getDescription();
                    log.warn("Query Explanation {}", exp);
                    tvm.spanStart = spans.startPosition();
                    tvm.spanEnd = spans.endPosition();
                    Terms terms = indexReader.getTermVector(spans.docID(), spanOrQuery.getField());
                    if (terms != null) {
                        tvm.map(terms, spans);
                    }
                    //the entries map contains the window, do some ranking of it
                    if (tvm.passage.terms.isEmpty() == false) {
                        log.debug("Candidate: Doc: {} Starts: {} End: {} ",
                                new Object[]{spans.docID(), spans.startPosition(), spans.endPosition()});
                    }
                    tvm.passage.lDocId = spans.docID();
                    tvm.passage.field = spanOrQuery.getField();
                    //score this window
                    try {
                        addPassage(tvm.passage, rankedPassages, termWeights, bigramWeights, adjWeight, secondAdjWeight, bigramWeight);
                    } catch (CloneNotSupportedException e) {
                        throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Internal error cloning Passage", e);
                    }
                    //clear out the entries for the next round
                    tvm.passage.clear();
                }
            }
            NamedList qaResp = new NamedList();
            responseBuilder.rsp.add("qaResponse", qaResp);
            int rows = params.getInt(OWL_ROWS, 5);
            SchemaField uniqField = responseBuilder.req.getSchema().getUniqueKeyField();
            if (rankedPassages.size() > 0) {
                int size = Math.min(rows, rankedPassages.size());
                Set<String> fields = new HashSet<String>();
                for (int i = size - 1; i >= 0; i--) {
                    Passage passage = rankedPassages.pop();
                    if (passage != null) {
                        NamedList passNL = new NamedList();
                        qaResp.add(("answer"), passNL);
                        String idName;
                        String idValue;
                        if (uniqField != null) {
                            idName = uniqField.getName();
                            fields.add(idName);
                            fields.add(passage.field);//prefetch this now, so that it is cached
                            idValue = searcher.doc(passage.lDocId, fields).get(idName);
                        } else {
                            idName = "luceneDocId";
                            idValue = String.valueOf(passage.lDocId);
                        }
                        //passNL.add(idName, idValue);
                        passNL.add("luceneDocId", passage.lDocId);
                        passNL.add("field", passage.field);
                        //get the window
                        String fldValue = searcher.doc(passage.lDocId, fields).get(passage.field);
                        if (fldValue != null) {
                            //get the window of words to display, we don't use the passage window, as that is based on the term vector
                            int start = passage.terms.first().start;//use the offsets
                            int end = passage.terms.last().end;
                            if (start >= 0 && start < fldValue.length() &&
                                    end >= 0 && end < fldValue.length()) {
                                passNL.add("window", fldValue.substring(start, end + passage.terms.last().term.length()));
                            } else {
                                log.warn("Passage does not have correct offset information");
                                passNL.add("window", fldValue);//we don't have offsets, or they are incorrect, return the whole field value
                            }
                        }
                    } else {
                        break;
                    }

                }
            }

        }

    }

    protected float scoreTerms(Set<WindowTerm> terms, Map<String, Float> termWeights, Set<String> covered) {
        float score = 0f;
        for (WindowTerm wTerm : terms) {
            Float tw = (Float) termWeights.get(wTerm.term);
            if (tw != null && !covered.contains(wTerm.term)) {
                score += tw.floatValue();
                covered.add(wTerm.term);
            }
        }

        return (score);
    }

    protected float scoreBigrams(Set<WindowTerm> bigrams, Map<String, Float> bigramWeights, Set<String> covered) {
        float result = 0;
        for (WindowTerm bigram : bigrams) {
            Float tw = (Float) bigramWeights.get(bigram.term);
            if (tw != null && !covered.contains(bigram.term)) {
                result += tw.floatValue();
                covered.add(bigram.term);
            }
        }
        return result;
    }

    /**
     * A fairly straightforward and simple scoring approach based on http://trec.nist.gov/pubs/trec8/papers/att-trec8.pdf.
     * <br/>
     * Score the {@link com.prasnottar.query.Passage} as the sum of:
     * <ul>
     * <li>The sum of the IDF values for the primary window terms ({@link com.prasnottar.query.Passage#terms}</li>
     * <li>The sum of the weights of the terms of the adjacent window ({@link com.prasnottar.query.Passage#prevTerms} and {@link com.prasnottar.query.Passage#followTerms}) * adjWeight</li>
     * <li>The sum of the weights terms of the second adjacent window ({@link com.prasnottar.query.Passage#secPrevTerms} and {@link com.prasnottar.query.Passage#secFollowTerms}) * secondAdjWeight</li>
     * <li>The sum of the weights of any bigram matches for the primary window * biWeight</li>
     * </ul>
     * In laymen's terms, this is a decay function that gives higher scores to matching terms that are closer to the anchor
     * term  (where the query matched, in the middle of the window) than those that are further away.
     *
     * @param p               The {@link com.prasnottar.query.Passage} to score
     * @param termWeights     The weights of the terms, key is the term, value is the inverse doc frequency (or other weight)
     * @param bigramWeights   The weights of the bigrams, key is the bigram, value is the weight
     * @param adjWeight       The weight to be applied to the adjacent window score
     * @param secondAdjWeight The weight to be applied to the secondary adjacent window score
     * @param biWeight        The weight to be applied to the bigram window score
     * @return The score of passage
     */
    //<start id="qa.scorePassage"/>
    protected float scorePassage(Passage p, Map<String, Float> termWeights,
                                 Map<String, Float> bigramWeights,
                                 float adjWeight, float secondAdjWeight,
                                 float biWeight) {
        Set<String> covered = new HashSet<String>();
        float termScore = scoreTerms(p.terms, termWeights, covered);//<co id="prc.main"/>
        float adjScore = scoreTerms(p.prevTerms, termWeights, covered) +
                scoreTerms(p.followTerms, termWeights, covered);//<co id="prc.adj"/>
        float secondScore = scoreTerms(p.secPrevTerms, termWeights, covered)
                + scoreTerms(p.secFollowTerms, termWeights, covered);//<co id="prc.sec"/>
        //Give a bonus for bigram matches in the main window, could also
        float bigramScore = scoreBigrams(p.bigrams, bigramWeights, covered);//<co id="prc.bigrams"/>
        float score = termScore + (adjWeight * adjScore) +
                (secondAdjWeight * secondScore)
                + (biWeight * bigramScore);//<co id="prc.score"/>
        return (score);
    }

    /**
     * Potentially add the passage to the PriorityQueue.
     *
     * @param p               The passage to add
     * @param pq              The {@link org.apache.lucene.util.PriorityQueue} to add the passage to if it ranks high enough
     * @param termWeights     The weights of the terms
     * @param bigramWeights   The weights of the bigrams
     * @param adjWeight       The weight to be applied to the score of the adjacent window
     * @param secondAdjWeight The weight to be applied to the score of the second adjacent window
     * @param biWeight        The weight to be applied to the score of the bigrams
     * @throws CloneNotSupportedException if not cloneable
     */
    private void addPassage(Passage p, PassagePriorityQueue pq, Map<String, Float> termWeights,
                            Map<String, Float> bigramWeights,
                            float adjWeight, float secondAdjWeight, float biWeight) throws CloneNotSupportedException {
        p.score = scorePassage(p, termWeights, bigramWeights, adjWeight, secondAdjWeight, biWeight);
        Passage lowest = pq.top();
        if (lowest == null || pq.lessThan(p, lowest) == false || pq.size() < pq.capacity()) {
            //by doing this, we can re-use the Passage object
            Passage cloned = (Passage) p.clone();
            //TODO: Do we care about the overflow?
            pq.insertWithOverflow(cloned);
        }

    }

    private void createWeights(String originalQuery, SpanOrQuery parsedQuery,
                               Map<String, Float> termWeights,
                               Map<String, Float> bigramWeights, IndexReader indexReader) throws IOException {
        SpanQuery[] clauses = parsedQuery.getClauses();
        //It returns the clauses which may have nested SpanNearQueries so
        //we need to recurse through the clauses until we get to SpanTermQuery
        Term lastTerm = null;
        Float lastWeight = null;
        for (int i = 0; i < clauses.length; i++) {
            SpanQuery clause = clauses[i];
            if (clause instanceof SpanTermQuery) {
                Term term = ((SpanTermQuery) clause).getTerm();
                Float weight = calculatedWeight(term, indexReader);
                termWeights.put(term.text(), weight);
                if (lastTerm != null) {//calculate the bi-grams
                    //use the smaller of the two weights
                    if (lastWeight.floatValue() < weight.floatValue()) {
                        bigramWeights.put(lastTerm + "," + term.text(), new Float(lastWeight.floatValue() * 0.25));
                    } else {
                        bigramWeights.put(lastTerm + "," + term.text(), new Float(weight.floatValue() * 0.25));
                    }
                }
                //last
                lastTerm = term;
                lastWeight = weight;
            } else if (clause instanceof SpanBoostQuery) {

                SpanTermQuery stq = (SpanTermQuery) ((SpanBoostQuery) clause).getQuery();
                Term term = stq.getTerm();
                Float weight = ((SpanBoostQuery) clause).getBoost() * calculatedWeight(term, indexReader);
                termWeights.put(term.text(), weight);
                if (lastTerm != null) {//calculate the bi-grams
                    //use the smaller of the two weights
                    if (lastWeight.floatValue() < weight.floatValue()) {
                        bigramWeights.put(lastTerm + "," + term.text(), new Float(lastWeight.floatValue() * 0.25));
                    } else {
                        bigramWeights.put(lastTerm + "," + term.text(), new Float(weight.floatValue() * 0.25));
                    }
                }
                //last
                lastTerm = term;
                lastWeight = weight;
            } else {
                //TODO: handle the other types
                throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Unhandled query type: " + clause.getClass().getName());
            }
        }

    }

    protected float calculatedWeight(Term term, IndexReader reader) throws IOException {
        //if a term is not in the index then it's weight is 0
        int docFrequency = reader.docFreq(term);
        if (docFrequency != 0) {
            log.warn("Term {} doc freq {} ", term.toString(), docFrequency);
            return 1.0f;
        } else {
            log.warn("Couldn't find doc freq for term {}", term);
            return 0f;
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

    class PassagePriorityQueue extends PriorityQueue<Passage> {

        PassagePriorityQueue() {
            super(10);
        }

        PassagePriorityQueue(int maxSize) {
            super(maxSize);
        }

        public int capacity() {
            return getHeapArray().length;
        }

        @Override
        public boolean lessThan(Passage passageA, Passage passageB) {
            if (passageA.score == passageB.score)
                return passageA.lDocId > passageB.lDocId;
            else
                return passageA.score < passageB.score;
        }

    }
}