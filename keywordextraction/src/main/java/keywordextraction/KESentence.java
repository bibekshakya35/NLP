package keywordextraction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KESentence {
    private String value;

    public KESentence(String value) {
        this.value = value;
    }

    public KEPhrase generatePhraseFrom(Pattern stopWordPattern) {
        Matcher stopMatcher = stopWordPattern.matcher(value);
        String sentenceWithoutStopwords = stopMatcher.replaceAll("|");
        return new KEPhrase().addWords(sentenceWithoutStopwords.split("\\|"));
    }
}
