package nlp.solr;

import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.util.Span;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * Tokenize input using the OPENNlp SentenceDetector
 */
public final class SentenceTokenizer extends Tokenizer {

    /**
     * Added @see {@link PositionIncrementAttribute}
     */
    private final PositionIncrementAttribute positionIncrementAttribute = addAttribute(PositionIncrementAttribute.class);
    private final CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);

    private SentenceDetector sentenceDetector;
    private Span[] sentenceSpans = null;
    private char[] inputSentence;
    private int tokenOffset = 0;

    public SentenceTokenizer(AttributeFactory attributeFactory, SentenceDetector detector) {
        super(attributeFactory);
        this.sentenceDetector = detector;
    }

    public SentenceTokenizer(Reader reader,SentenceDetector sentenceDetector) {
        super();
        this.input =reader;
        this.sentenceDetector = sentenceDetector;
    }
    public void reader(Reader reader){
        this.input = reader;
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        sentenceDetector = null;
    }

    public void fillSentences() throws IOException {
        char[] c = new char[256];
        int size = 0;
        StringBuilder stringBuilder = new StringBuilder();
        while ((size = input.read(c)) >= 0) {
            stringBuilder.append(c, 0, size);
        }
        String temp = stringBuilder.toString();
        inputSentence = temp.toCharArray();
        sentenceSpans = sentenceDetector.sentPosDetect(temp);
        tokenOffset = 0;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (sentenceSpans == null) {
            fillSentences();
        }
        if (tokenOffset >= sentenceSpans.length) {
            return false;
        }
        Span sentenceSpan = sentenceSpans[tokenOffset];
        clearAttributes();
        int start = sentenceSpan.getStart();
        int end = sentenceSpan.getEnd();
        charTermAttribute.copyBuffer(inputSentence, start, end - start);
        positionIncrementAttribute.setPositionIncrement(1);
        offsetAttribute.setOffset(start, end);
        tokenOffset++;
        return true;
    }
}
