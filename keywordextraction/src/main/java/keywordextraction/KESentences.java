package keywordextraction;

import java.util.Iterator;

/**
 * Take sentence from source for keyword extraction
 */
public class KESentences implements Iterator<KESentence> {
    private KESentence[] sentences;
    private int currentPosition;

    public KESentences() {

    }

    public KESentences addSentences(String[] values) {
        if (values != null) {
            this.sentences = new KESentence[values.length];
            int count = 0;
            for (String sentence : values) {
                this.sentences[count++] = new KESentence(sentence);
            }
        }
        return this;
    }

    public KESentences iterator() {
        currentPosition = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < sentences.length;
    }

    @Override
    public KESentence next() {
        KESentence current = sentences[currentPosition];
        currentPosition++;
        return current;
    }
}
