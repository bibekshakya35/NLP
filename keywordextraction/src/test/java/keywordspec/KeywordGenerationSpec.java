package keywordspec;

import keywordextraction.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

public class KeywordGenerationSpec {
    private final String input = String.format("%s%s%s%s%s%s",
            "Compatible of systems of linear constraints over the set of natural numbers.",
            " Criteria of compatibility of a system of linear Diophantine equations, strict inequations,",
            " and nonstrict inequations are considered. Upper bounds for components of a minimal set of solutions",
            " and algorithms of construction of minimal generating sets of solutions for all types of systems are given.",
            " These criteria and the corresponding algorithms for constructing a minimal supporting set of solutions ",
            "can be used in solving all the considered types of systems and systems of mixed types.");

    @Test
    public void generateKeyword() throws IOException {
        KeywordRelevance keywordRelevance = new KeywordRelevance();
        final KESentences sentences
                = new KESentenceTokenizer().split(input);
        final KEStopList stopList = new KEStopList()
                .generateStopwords(new FileUtil("SmartStoplist.txt"));
        final KECandidates candidates =
                new KECandidates()
                        .generateKeywords(sentences, stopList.getStopwords());
        Assert.assertNotNull(candidates);
        final Map<String, Double> wordScore = keywordRelevance.calculateWordScores(candidates.getPhraseList());
        Assert.assertNotNull(wordScore);
        final Map<String, Double> keywordCandidates =
                keywordRelevance.generateCandidateKeywordScores(candidates.getPhraseList(), wordScore);
        Map<String,Double> sortedCandidateKeyword = keywordRelevance.sortKeywordCandidates(keywordCandidates);
        Assert.assertNotNull(sortedCandidateKeyword);
        System.out.println(sortedCandidateKeyword.toString());
    }
}
