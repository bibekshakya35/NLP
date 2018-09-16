package nlp.qa;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import opennlp.tools.parser.Parser;
import org.apache.solr.search.SyntaxError;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The QuestionQParser takes in a natural language question and produces a Lucene {@link org.apache.lucene.search.spans.SpanNearQuery}
 */
public class QuestionQParser extends QParser implements QAParams {
    private Parser parser;
    private AnswerTypeClassifier answerTypeClassifier;
    private Map<String, String> atm;

    public QuestionQParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req,
                           Parser parser, AnswerTypeClassifier atc,
                           Map<String, String> answerTypeMap) {
        super(qstr, localParams, params, req);
        this.parser = parser;
        this.answerTypeClassifier = atc;
        this.atm = answerTypeMap;
    }

    @Override
    public Query parse() throws SyntaxError {
        //Parse the question using the {@link TreebankParser}.
        // The resulting {@link Parse} object can then be utilized by the classifier to determine the Answer Type.
        Parse parse = ParserTool.parseLine(qstr, parser, 1)[0];
        String type = answerTypeClassifier.computeAnswerType(parse);
        String mt = atm.get(QUERY_FIELD);
        String field = params.get(QUERY_FIELD);
        SchemaField sp = req.getSchema().getFieldOrNull(field);
        if (sp == null) throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "undefined field " + field);
        List<SpanQuery> sql = new ArrayList<>();
        if (mt != null) {
            String[] parts = mt.split("\\|");
            if (parts
                    .length == 1) sql.add(new SpanTermQuery(new Term(field, mt.toLowerCase())));
            else {
                for (int i = 0; i < parts.length; i++) {
                    sql.add(new SpanTermQuery(new Term(field, parts[i])));
                }
            }
        }
        try {
            Analyzer analyzer = sp.getType().getQueryAnalyzer();
            TokenStream tokenStream = analyzer.tokenStream(field, new StringReader(qstr));
            while (tokenStream.incrementToken()) {
                String term = ((CharTermAttribute) tokenStream.getAttribute(CharTermAttribute.class)).toString();
                sql.add(new SpanTermQuery(new Term(field, term)));
            }
        } catch (IOException io) {
            throw new SyntaxError(io.getLocalizedMessage());
        }
        return new SpanNearQuery(sql.toArray(new SpanNearQuery[sql.size()]), params.getInt(QAParams.SLOP, 10), true);
    }
}
