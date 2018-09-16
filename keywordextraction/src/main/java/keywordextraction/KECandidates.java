package keywordextraction;

import java.util.List;
import java.util.regex.Pattern;

public class KECandidates {
    private Keywords keywords;

    public KECandidates() {
        this.keywords = new Keywords();
    }

    public KECandidates generateKeywords(KESentences sentences, KEStopwords stopwords) {
        KESentences sentenceItr = sentences.iterator();
        while (sentenceItr.hasNext()) {
            final KESentence sentence = sentenceItr.next();
            final Pattern pattern = stopwords.getStopwordPattern();
            KEPhrase phrase = sentence.generatePhraseFrom(pattern);
            addKeywords(phrase);
        }
        return this;
    }

    private void addKeywords(KEPhrase phrase) {
        if (phrase != null && !phrase.isEmpty()) {
            phrase = phrase.iterator();
            addKeyword(phrase);
        }
    }

    private void addKeyword(KEPhrase phrase) {
        while (phrase.hasNext()) {
            final KEWord word = phrase.next();
            validateAndAdd(word);
        }
    }

    private void validateAndAdd(KEWord word) {
        if (!word.isEmpty()) {
            keywords.add(word.getAsLowerCase());
        }
    }

    public List<String> getPhraseList() {
        return keywords.getKeywords();
    }
}
