

package nlp.solr;

import nlp.utils.SentenceDetectorFactory;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

public class SentenceTokenizerFactory extends TokenizerFactory {

    SentenceDetectorFactory sentenceDetectorFactory;
    private Reader reader;

    public SentenceTokenizerFactory(Map<String, String> args) {
        super(args);
        try {
            sentenceDetectorFactory = new SentenceDetectorFactory(args);
        } catch (IOException e) {
            throw (RuntimeException) new RuntimeException().initCause(e);
        }
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        SentenceTokenizer sentenceTokenizer = new SentenceTokenizer(factory, sentenceDetectorFactory.getSentenceDetector());
        sentenceTokenizer.reader(reader);
        return sentenceTokenizer;
    }
}