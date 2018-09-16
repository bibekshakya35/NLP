package nlp.qa;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Span;

/**
 * Finds flat chunk instead of a tree structure using a simpler model.
 * This class is not thread-safe, but should be lightweight to construct
 */
public class ChunkParser implements Parser {
    private ChunkerME chunker;
    private POSTaggerME tagger;

    public ChunkParser(ChunkerME chunkerME, POSTaggerME tagger) {
        this.chunker = chunkerME;
        this.tagger = tagger;
    }

    @Override
    public Parse[] parse(Parse tokens, int numParses) {
        //TODO: get multiple tag sequences and chunk each.
        return new Parse[]{parse(tokens)};
    }

    @Override
    public Parse parse(Parse tokens) {
        Parse[] children = tokens.getChildren();
        String[] words = new String[children.length];
        double[] probs = new double[words.length];
        for (int i = 0, il = children.length; i < il; i++) {
            words[i] = children[i].toString();
        }
        String[] tags = tagger.tag(words);
        tagger.probs(probs);//<co id="cp.probs"/>
        for (int j = 0; j < words.length; j++) {
            Parse word = children[j];
            double prob = probs[j];
            tokens.insert(new Parse(word.getText(), word.getSpan(), tags[j], prob, j));
            tokens.addProb(Math.log(prob));
        }

        String[] chunks = chunker.chunk(words, tags);
        chunker.probs(probs);
        int chunkStart = -1;
        String chunkType = null;
        double logProb=0;
        for (int ci=0,cn=chunks.length;ci<cn;ci++) {
            if (ci > 0 && !chunks[ci].startsWith("I-") && !chunks[ci-1].equals("O")) {
                Span span = new Span(children[chunkStart].getSpan().getStart(),children[ci-1].getSpan().getEnd());
                tokens.insert(new Parse(tokens.getText(), span, chunkType, logProb,children[ci-1]));
                logProb=0;
            }
            if (chunks[ci].startsWith("B-")) {
                chunkStart = ci;
                chunkType = chunks[ci].substring(2);
            }
            logProb+=Math.log(probs[ci]);
        }
        if (!chunks[chunks.length-1].equals("O")) {
            int ci = chunks.length;
            Span span = new Span(children[chunkStart].getSpan().getStart(),children[ci-1].getSpan().getEnd());
            tokens.insert(new Parse(tokens.getText(), span, chunkType, logProb,children[ci-1]));
        }
        return tokens;
    }
}
