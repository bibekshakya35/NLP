package keywordextraction;

import java.util.Iterator;

public class KEPhrase implements Iterator<KEWord>{
    private KEWord[] words;
    private int currentPosition;

    public KEPhrase() {
    }

    /**
     *
     * @param values
     * @return
     */
    public KEPhrase addWords(String[] values) {
        if (values != null) {
            this.words = new KEWord[values.length];
            int i = 0;
            for (String value : values) {
                this.words[i++] = new KEWord(value);
            }
        }
        return this;
    }
    public boolean isEmpty(){
        return words==null|| words.length==0;
    }
    public KEPhrase iterator(){
        currentPosition = 0;
        return this;
    }

    @Override
    public boolean hasNext() {
        return currentPosition < words.length;
    }

    @Override
    public KEWord next() {
        KEWord current =  words[currentPosition];
        currentPosition++;
        return current;
    }
}
