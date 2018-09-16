package com.prasnottar.solr;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.TimeAnnotator;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import java.util.Map;
import java.util.Properties;
@Slf4j
public class StanfordFactory extends TokenFilterFactory {
    private StanfordCoreNLP pipeline = null;
    public StanfordFactory(Map<String, String> args) {
        super(args);
        log.info("Inside constructor");
        //following annotators to be used
        //tokenize
        //cleanxml : Remove xml tokens from the document.
        // May use them to mark sentence ends or to extract metadata.
        //ssplit : Splits a sequence of tokens into sentences.
        //pos : part of speech
        //lemma : Generates the word lemmas for all tokens in the corpus.
        //ner : Name Entity Recognition
        Properties properties = new Properties();
        properties.setProperty("annotators","tokenize, cleanxml,ssplit,pos,lemma,ner");
        properties.setProperty("ner.useSUTime", "false");
        this.pipeline = new StanfordCoreNLP(properties);

        this.pipeline.addAnnotator(new TimeAnnotator("sutime", properties));
    }

    @Override
    public TokenStream create(TokenStream tokenStream) {
        log.info("Hereeeeee");
        return new StanfordFilter(tokenStream,pipeline);
    }

}
