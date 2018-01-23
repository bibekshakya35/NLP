package keywordextraction;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class KeywordRelevance {
    private List<String> seperateWords(final String text, final int minimumWordReturnSize) {
        final String regex = "[^a-zA-Z0-9_\\+\\-/]";
        final List<String> seperateWords = new ArrayList<>();
        final String[] words = text.split(regex);
        if (words != null && words.length > 0) {
            for (final String word : words) {
                String wordLowerCase = word.trim().toLowerCase();
                if (wordLowerCase.length() > 0 && wordLowerCase.length() > minimumWordReturnSize
                        && !StringUtils.isNumeric(wordLowerCase)) {
                    seperateWords.add(wordLowerCase);
                }
            }
        }
        return seperateWords;
    }

    public Map<String, Double> calculateWordScores(List<String> phraseList) {
        final Map<String, Integer> wordFrequency = new HashMap<>();
        final Map<String, Integer> wordDegree = new HashMap<>();
        final Map<String, Double> wordScore = new HashMap<>();
        for (final String phrase : phraseList) {
            final List<String> wordList = seperateWords(phrase, 0);
            final int wordListLength = wordList.size();
            final int wordListDegree = wordListLength - 1;
            for (final String word : wordList) {
                if (!wordFrequency.containsKey(word)) {
                    wordFrequency.put(word, 0);
                }
                if (!wordDegree.containsKey(word)) {
                    wordDegree.put(word, 0);
                }
                wordFrequency.put(word, wordFrequency.get(word) + 1);
                wordDegree.put(word, wordDegree.get(word) + wordListDegree);
            }
        }
        final Iterator<String> wordIterator = wordFrequency.keySet().iterator();
        while (wordIterator.hasNext()) {
            final String word = wordIterator.next();
            wordDegree.put(word, wordDegree.get(word) + wordFrequency.get(word));
            if (!wordScore.containsKey(word)) {
                wordScore.put(word, 0.0);
            }
            wordScore.put(word, wordDegree.get(word) / (wordFrequency.get(word) * 1.0));
        }
        return wordScore;
    }

    public Map<String, Double> generateCandidateKeywordScores(List<String> phraseList,
                                                              Map<String, Double> wordScore) {
        final Map<String, Double> keywordCandidates = new HashMap<>();
        for (String phrase : phraseList) {
            final List<String> wordList = seperateWords(phrase, 0);
            double candidateScore = 0;
            for (final String word : wordList) {
                candidateScore += wordScore.get(word);
            }
            keywordCandidates.put(phrase, candidateScore);
        }
        return keywordCandidates;
    }

    public Map<String, Double> sortKeywordCandidates(
            Map<String, Double> keywordCandidates
    ) {
        final Map<String, Double> sortedKeywordCandidates = new LinkedHashMap<>();
        int totalKeywordCandidates = keywordCandidates.size();
        final List<Map.Entry<String, Double>> keywordCandidatesAsList =
                new LinkedList<Map.Entry<String, Double>>(keywordCandidates.entrySet());
        Collections.sort(keywordCandidatesAsList, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return ((Map.Entry<String, Double>) o2).getValue()
                        .compareTo(((Map.Entry<String, Double>) o1).getValue());
            }
        });
        //provide
        totalKeywordCandidates = totalKeywordCandidates / 2;
        for (final Map.Entry<String, Double> entry : keywordCandidatesAsList) {
            sortedKeywordCandidates.put(entry.getKey(), entry.getValue());
            if (--totalKeywordCandidates == 0) {
                break;
            }
        }
        return sortedKeywordCandidates;
    }
}
