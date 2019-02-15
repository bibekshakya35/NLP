package io.github.bibekshakya35.standford.nlp.sentencerecognizer;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import io.github.bibekshakya35.standford.nlp.Pipeline;

public class SentenceRecognizer {
    public static void main(String[] args){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        String text = "Hello! My name is slim shady. I am a illest rapper ever alive.";
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        coreDocument.sentences().forEach(coreSentence -> {
            System.out.println(coreSentence.toString());
        });
    }
}
