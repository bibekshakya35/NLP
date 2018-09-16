package keywordextraction;

public class KESentenceTokenizer {
    private String grammar;

    public KESentenceTokenizer() {
        this.grammar = "[.!?,;:\\t\\\\-\\\\\"\\\\(\\\\)\\\\\\'\\u2019\\u2013]";
    }

    public KESentenceTokenizer(String grammar) {
        this.grammar = grammar;
    }

    public KESentences split(String input) {
        return new KESentences()
                .addSentences(input.split(this.grammar));
    }
}
