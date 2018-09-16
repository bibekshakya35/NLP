package com.prasnottar.query;

import lombok.extern.slf4j.Slf4j;
import opennlp.maxent.io.SuffixSensitiveGISModelReader;
import opennlp.model.MaxentModel;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.parser.Parser;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PrasnottarQParserPlugin extends QParserPlugin implements PrasnottarParam {
    private Map<String, String> answerTypeMap;
    // protected GISModel model;
    protected MaxentModel model;
    protected double[] probs;
    protected AnswerTypeContextGenerator atcg;
    private POSTaggerME tagger;
    private ChunkerME chunker;

    //<start id="qqpp.create"/>
    @Override
    public QParser createParser(String qStr, SolrParams localParams, SolrParams params,
                                SolrQueryRequest req) {
        answerTypeMap = new HashMap<String, String>();
        answerTypeMap.put("L", "NE_LOCATION");
        answerTypeMap.put("T", "NE_TIME|NE_DATE");
        answerTypeMap.put("P", "NE_PERSON");
        answerTypeMap.put("M", "NE_MONEY");
        answerTypeMap.put("O", "NE_ORGANIZATION");
        answerTypeMap.put("L", "NE_LOCATION");
        answerTypeMap.put("C", "NE_PERCENT");
        answerTypeMap.put("F", "DESCRIPTION");
        answerTypeMap.put("X", "OTHERS");
        QParser qParser;
        if (params.getBool(COMPONENT_NAME, false) == true
                && qStr.equals("*:*") == false) {
            AnswerTypeClassifier atc =
                    new AnswerTypeClassifier(model, probs, atcg);
            Parser parser = new ChunkParser(chunker, tagger);
            qParser = new PrasnottarQParser(qStr, localParams,
                    params, req, parser, atc, answerTypeMap);
        } else {
            //just do a regular query if Prasnottar is turned off
            qParser = req.getCore().getQueryPlugin("edismax")
                    .createParser(qStr, localParams, params, req);
        }
        return qParser;
    }
    public void init(NamedList initArgs) {
        SolrParams params = SolrParams.toSolrParams(initArgs);
        String modelDirectory = params.get("modelDirectory",
                System.getProperty("model.dir"));//<co id="qqpp.model"/>
        String wordnetDirectory = params.get("wordnetDirectory",
                System.getProperty("wordnet.dir"));//<co id="qqpp.wordnet"/>
        if (modelDirectory != null) {
            File modelsDir = new File(modelDirectory);
            try {
                InputStream chunkerStream = new FileInputStream(
                        new File(modelsDir,"en-chunker.bin"));
                ChunkerModel chunkerModel = new ChunkerModel(chunkerStream);
                chunker = new ChunkerME(chunkerModel); //<co id="qqpp.chunker"/>
                InputStream posStream = new FileInputStream(
                        new File(modelsDir,"en-pos-maxent.bin"));
                POSModel posModel = new POSModel(posStream);
                tagger =  new POSTaggerME(posModel); //

                model = new SuffixSensitiveGISModelReader(new File(modelDirectory+"/qa/ans.bin")).getModel();
                //GISModel m = new SuffixSensitiveGISModelReader(new File(modelFileName)).getModel();
                probs = new double[model.getNumOutcomes()];
                atcg = new AnswerTypeContextGenerator(
                        new File(wordnetDirectory, "dict"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
