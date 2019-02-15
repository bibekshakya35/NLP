package io.github.bibekshakya35.stanford.nlp.partofspeech;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.github.bibekshakya35.stanford.nlp.Pipeline;

public class PartOfSpeech {
    public static void main(String[] args) {
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hello! My name is slim shady. I am a illest rapper ever alive.";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        coreDocument.tokens().forEach(coreLabel -> {
            String pos = coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            System.out.println(coreLabel.originalText() + " = " + pos);
        });
    }
}
