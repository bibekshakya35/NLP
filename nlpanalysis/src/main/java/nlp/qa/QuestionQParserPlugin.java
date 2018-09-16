package nlp.qa;


import opennlp.model.MaxentModel;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.parser.Parser;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import org.apache.commons.lang.StringUtils;
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

public class QuestionQParserPlugin extends QParserPlugin {
    private Map<String, String> answerTypeMap;
    protected MaxentModel model;
    protected double[] probs;
    protected AnswerTypeContextGenerator answerTypeContextGenerator;
    private POSTaggerME tagger;
    private ChunkerME chunker;

    @Override
    public QParser createParser(String qStr, SolrParams localParams, SolrParams params, SolrQueryRequest request) {
        //Construct a map of the answer types that we are interested in handling, for instance locations, people and times and dates.
        answerTypeMap = new HashMap<>();
        answerTypeMap.put("L", "NE_LOCATION");
        answerTypeMap.put("T", "NE_TIMELINE_DATE");
        answerTypeMap.put("P", "NE_PERSON");
        answerTypeMap.put("0", "NE_ORGANIZATION");

        QParser qParser;
        //We use this if clause to create an regular Solr query parser in the cases
        //where the user hasn't entered a question or the enter the *:* query
        if (params.getBool(QAParams.COMPONENT_NAME, false)
                && !qStr.equals("*:*")) {
            //The AnswerTypeClassifier uses the trained Answer Type model (located in the models directory) to classify the question.
            AnswerTypeClassifier atc =
                    new AnswerTypeClassifier(model, probs, answerTypeContextGenerator);
            //Construct the chunker (parser) that will be responsible for parsing the user question.
            Parser parser = new ChunkParser(chunker, tagger);
            //Create the QuestionQParser by passing in the user's question as well as the pre-initialized resources from the init method.
            qParser = new QuestionQParser(qStr, localParams,
                    params, request,
                    parser, atc,
                    answerTypeMap);
        } else {
            //do regular query if qa is turn off
            qParser =
                    request.getCore()
                            .getQueryPlugin("edismax")
                            .createParser(qStr, localParams, params, request);
        }
        return qParser;
    }

    @Override
    public void init(NamedList args) {
        SolrParams params = SolrParams.toSolrParams(args);
        String modelDirectory = params.get("modelDirectory",
                System.getProperty("model.dir"));
        String wordnetDirectory = params.get("wordnetDirectory",
                System.getProperty("wordnet.dir"));
        if (StringUtils.isNotBlank(modelDirectory)) {
            File modelDir = new File(modelDirectory);
            try {
                InputStream chunkerStream = new FileInputStream(new File(modelDir, "en-chunker.bin"));
                ChunkerModel chunkerModel = new ChunkerModel(chunkerStream);
                chunker = new ChunkerME(chunkerModel);
                InputStream posStream = new FileInputStream(
                        new File(modelDir, "en-pos-maxent.bin"));
                POSModel posModel = new POSModel(posStream);
                tagger = new POSTaggerME(posModel);
                model = new DoccatModel(new FileInputStream(new File(modelDir, "en-answer.bin")))
                        .getChunkerModel();
                probs = new double[model.getNumOutcomes()];
                answerTypeContextGenerator =
                        new AnswerTypeContextGenerator(new File(wordnetDirectory));
            } catch (IOException io) {
                throw new RuntimeException(io);
            }
        }
    }
}
