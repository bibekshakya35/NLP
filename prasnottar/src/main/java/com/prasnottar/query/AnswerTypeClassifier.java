package com.prasnottar.query;

import opennlp.maxent.GIS;
import opennlp.maxent.GISModel;
import opennlp.maxent.io.GISModelWriter;
import opennlp.maxent.io.SuffixSensitiveGISModelWriter;
import opennlp.model.MaxentModel;
import opennlp.model.TwoPassDataIndexer;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AnswerTypeClassifier {
    private MaxentModel model;
    private double[] probs;
    private AnswerTypeContextGenerator answerTypeContextGenerator;

    public AnswerTypeClassifier(MaxentModel model, double[] probs, AnswerTypeContextGenerator answerTypeContextGenerator) {
        this.model = model;
        this.probs = probs;
        this.answerTypeContextGenerator = answerTypeContextGenerator;
    }
    public String computeAnswerType(Parse question) {
        double[] probs = computeAnswerTypeProbs(question);
        return model.getBestOutcome(probs);
    }
    public double[] computeAnswerTypeProbs(Parse question) {
        String[] context = answerTypeContextGenerator.getContext(question);
        return model.eval(context, probs);
    }
    /**
     * Train the answer model
     * <p>
     * Hint:
     * <pre>
     *  mvn exec:java -Dexec.mainClass=com.tamingtext.qa.AnswerTypeClassifier \
     *    -Dexec.args="dist/data/questions-train.txt en-answer.bin" \
     *    -Dmodel.dir=../../opennlp-models \
     *    -Dwordnet.dir=../../Wordnet-3.0/dict
     *  </pre>
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String trainFile = "/home/bibek/bs-workspace/NLP/prasnottar/data/questions-train.txt";
        File outFile = new File("/home/bibek/bs-workspace/NLP/prasnottar/opennlp-models/qa/answer.bin");
        String modelsDirProp = "/home/bibek/bs-workspace/NLP/prasnottar/opennlp-models";
        File modelsDir = new File(modelsDirProp);
        String wordnetDir = "/home/bibek/bs-workspace/NLP/prasnottar/WordNet-3.0/dict";

        InputStream chunkerStream = new FileInputStream(
                new File(modelsDir, "en-chunker.bin"));
        ChunkerModel chunkerModel = new ChunkerModel(chunkerStream);
        ChunkerME chunker = new ChunkerME(chunkerModel);
        InputStream posStream = new FileInputStream(
                new File(modelsDir, "en-pos-maxent.bin"));
        POSModel posModel = new POSModel(posStream);
        POSTaggerME tagger = new POSTaggerME(posModel);
        Parser parser = new ChunkParser(chunker, tagger);
        AnswerTypeContextGenerator actg = new AnswerTypeContextGenerator(new File(wordnetDir));

        AnswerTypeEventStream es = new AnswerTypeEventStream(trainFile,
                actg, parser);
        GISModel model = GIS.trainModel(100, new TwoPassDataIndexer(es, 3));//<co id="atc.train.do"/>
        GISModelWriter writer = new SuffixSensitiveGISModelWriter(model, outFile);
        writer.persist();

    }
}
