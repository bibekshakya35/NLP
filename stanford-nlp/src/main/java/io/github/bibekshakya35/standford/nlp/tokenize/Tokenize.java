package io.github.bibekshakya35.standford.nlp.tokenize;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.github.bibekshakya35.standford.nlp.Pipeline;

public class Tokenize {
    public static void main(String[] args){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hello! My name is slim shady.";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        coreDocument.tokens().forEach(coreLabel -> {
            System.out.println(coreLabel.originalText());
        });
    }
}
