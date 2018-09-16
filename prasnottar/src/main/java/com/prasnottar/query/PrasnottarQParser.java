package com.prasnottar.query;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.spans.SpanBoostQuery;
import org.apache.lucene.search.spans.SpanOrQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.schema.SchemaField;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The QuestionQParser takes in a natural language question
 * and produces a Lucene {@link org.apache.lucene.search.spans.SpanNearQuery}
 */
@Slf4j
public class PrasnottarQParser extends QParser implements PrasnottarParam {
    private Parser parser;
    private AnswerTypeClassifier atc;
    private Map<String, String> atm;

    public PrasnottarQParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req,
                             Parser parser, AnswerTypeClassifier atc,
                             Map<String, String> answerTypeMap) {
        super(qstr, localParams, params, req);
        this.parser = parser;
        this.atc = atc;
        this.atm = answerTypeMap;
    }

    public Query parse() throws SyntaxError {
        Parse parse = ParserTool.parseLine(qstr, parser, 1)[0];
        String type = atc.computeAnswerType(parse);
        String mt = atm.get(type);
        if (mt.equals("DESCRIPTION")) {
            BooleanQuery bq;
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            String field = "text";
            SchemaField sf = req.getSchema().getFieldOrNull(field);
            try {
                Analyzer analyzer = sf.getType().getQueryAnalyzer();
                TokenStream tokenStream =
                        analyzer.tokenStream(field, new StringReader(qstr));
                tokenStream.reset();
                CharTermAttribute tok = null;
                while (tokenStream.incrementToken()) {
                    tok = tokenStream.getAttribute(CharTermAttribute.class);
                    String term = tok.toString();
                    builder.add(new TermQuery(new Term(field, term)), BooleanClause.Occur.SHOULD);
                }
                tokenStream.close();
            } catch (IOException io) {
                throw new SyntaxError(io.getLocalizedMessage());
            }
            bq = builder.build();
            return bq;
        } else {
            String field = "text";
            SchemaField sp = req.getSchema().getFieldOrNull(field);
            if (sp == null) {
                throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Undefined field: " + field);
            }
            List<SpanQuery> sql = new ArrayList<SpanQuery>();
            if (mt != null) {
                String[] parts = mt.split("\\|");
                if (parts.length == 1) {
                    sql.add(new SpanTermQuery(new Term(field, mt.toLowerCase())));
                } else {
                    for (int pi = 0; pi < parts.length; pi++) {
                        sql.add(new SpanTermQuery(new Term(field, parts[pi].toLowerCase())));
                    }
                }
            }
            log.warn("answer type mt : {} {} ", mt, type);
            FocusNoun fn = new FocusNoun();
            String fnn[] = null;
            try {
                fnn = fn.getFocusNoun(qstr);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                Analyzer analyzer = sp.getType().getQueryAnalyzer();
                TokenStream ts = analyzer.tokenStream(field,
                        new StringReader(qstr));
                ts.reset();
                CharTermAttribute tok = null;

                while (ts.incrementToken()) {//<co id="qqp.addTerms"/>
                    tok = ts.getAttribute(CharTermAttribute.class);
                    String term = tok.toString();
                    log.warn("terms boosted {} ", term);
                    if (fnn != null)
                        if (term.equals(fnn[0]) || term.equals(fnn[1])) {
                            SpanQuery sq = new SpanTermQuery(new Term(field, term));
                            sql.add(new SpanBoostQuery(sq, 100f));
                        } else {
                            SpanQuery sq = new SpanTermQuery(new Term(field, term));
                            sql.add(new SpanBoostQuery(sq, 5f));
                        }

                    // sql.add(new SpanTermQuery(new Term(field, term)));
                }
                ts.close();
            } catch (IOException e) {
                throw new SyntaxError(e.getLocalizedMessage());
            }
            return new SpanOrQuery(sql.toArray(new SpanQuery[sql.size()]));
        }
    }
}
