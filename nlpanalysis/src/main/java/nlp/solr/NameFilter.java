package nlp.solr;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

public final class NameFilter extends TokenFilter {
    public static final String NE_PREFIX = "NE_";
    private final Tokenizer tokenizer;
    private final String[] tokenTypeNames;
    private final NameFinderME[] nameFinderME;
    private final KeywordAttribute keywordAttribute = addAttribute(KeywordAttribute.class);
    private final PositionIncrementAttribute positionIncrementAttribute = addAttribute(PositionIncrementAttribute.class);
    private final CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAttribute = addAttribute(OffsetAttribute.class);

    private String text;
    private int baseOffset;

    private Span[] spans;
    private String[] tokens;
    private Span[][] foundNames;

    private boolean[][] tokenTypes;

    private int spanOffsets = 0;
    private final Queue<AttributeSource.State> tokenQueue =
            new LinkedList<>();

    public NameFilter(TokenStream in, String[] modelNames,
                      NameFinderME[] nameFinderME) {
        super(in);
        this.tokenizer = SimpleTokenizer.INSTANCE;
        this.nameFinderME = nameFinderME;
        this.tokenTypeNames = new String[modelNames.length];
        for (int i = 0; i < modelNames.length; i++) {
            this.tokenTypeNames[i] = NE_PREFIX + modelNames[i];
        }
    }

    //consumes tokens from the upstream tokenizer and buffer them in a
    //StringBuilder whose contents will be passed to opennlp
    protected boolean fillSpans() throws IOException {
        if (!this.input.incrementToken()) return false;
        //process the next nlp.sentence from the upstream tokenizer
        this.text = input.getAttribute(CharTermAttribute.class).toString();
        this.baseOffset = this.input.getAttribute(OffsetAttribute.class).startOffset();
        this.spans = this.tokenizer.tokenizePos(text);
        this.tokens = Span.spansToStrings(spans, text);
        this.foundNames = new Span[this.nameFinderME.length][];
        for (int i = 0; i < nameFinderME.length; i++) {
            this.foundNames[i] = nameFinderME[i].find(tokens);
        }
        //insize
        this.tokenTypes = new boolean[this.tokens.length][this.nameFinderME.length];
        for (int i = 0; i < nameFinderME.length; i++) {
            Span[] spans = foundNames[i];
            for (int j = 0; j < spans.length; j++) {
                int start = spans[j].getStart();
                int end = spans[j].getEnd();
                for (int k = start; k < end; k++) {
                    this.tokenTypes[k][i] = true;
                }
            }
        }
        spanOffsets = 0;
        return true;
    }

    @Override
    public boolean incrementToken() throws IOException {
        //if there's nothing in the queue
        if(tokenQueue.peek()==null){
            //no span or spans consumed
            if (spans==null||spanOffsets>=spans.length){
                //no more data to fill spans
                if (!fillSpans())return false;
            }
            if (spanOffsets>=spans.length)return false;
            //copy the token and any types
            clearAttributes();
            keywordAttribute.setKeyword(false);
            positionIncrementAttribute.setPositionIncrement(1);
            int startOffset = baseOffset +spans[spanOffsets].getStart();
            int endOffset = baseOffset+spans[spanOffsets].getEnd();
            offsetAttribute.setOffset(startOffset,endOffset);
            charTermAttribute.setEmpty()
                    .append(tokens[spanOffsets]);
            //determine of the current token is of a named entity type, if so
            //push the current state into the queue and add a token reflecting
            // any matching entity types.
            boolean [] types = tokenTypes[spanOffsets];
            for (int i = 0; i < nameFinderME.length; i++) {
                if (types[i]){
                    keywordAttribute.setKeyword(true);
                    positionIncrementAttribute.setPositionIncrement(0);
                    tokenQueue.add(captureState());
                    positionIncrementAttribute.setPositionIncrement(1);
                    charTermAttribute.setEmpty().append(tokenTypeNames[i]);
                }
            }

            spanOffsets++;
            return true;
        }
        State state = tokenQueue.poll();
        restoreState(state);
        return true;
    }

    @Override
    public void close() throws IOException {
        super.close();
        reset();
    }
    @Override
    public void reset() throws IOException {
        super.reset();
        this.spanOffsets = 0;
        this.spans = null;
    }

    @Override
    public void end() throws IOException {
        super.end();
        reset();
    }

    protected void dumpState() {
        System.out.println(text);
        System.out.println("---");
        for (int i = 0; i < spans.length; i++) {
            System.out.println(i + " ; " + spans[i].getStart() + " : " + spans[i].getEnd() + " : " + tokens[i] + "'");
        }
        System.err.println("--");
        for (int i = 0; i < foundNames.length; i++) {
            System.out.println(tokenTypeNames[i]);
            for (int j = 0; j < foundNames[i].length; j++) {
                int start = foundNames[i][j].getStart();
                int end   = foundNames[i][j].getEnd();
                System.err.println("\t" + start + ":" + end);
                for (int k = start; k < end; k++) {
                    System.err.println("\t\t" + k + ":'" + tokens[k] + "'");
                }
            }
            System.err.println("--");
        }
        System.err.println("-------------------------------------");
    }

}
