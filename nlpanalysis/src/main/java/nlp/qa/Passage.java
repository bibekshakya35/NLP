package nlp.qa;

import java.util.SortedSet;
import java.util.TreeSet;

class Passage implements Cloneable {
    int lDocId;
    String field;

    float score;
    SortedSet<PassageRankingComponent.WindowTerm> terms = new TreeSet<PassageRankingComponent.WindowTerm>();
    SortedSet<PassageRankingComponent.WindowTerm> prevTerms = new TreeSet<PassageRankingComponent.WindowTerm>();
    SortedSet<PassageRankingComponent.WindowTerm> followTerms = new TreeSet<PassageRankingComponent.WindowTerm>();
    SortedSet<PassageRankingComponent.WindowTerm> secPrevTerms = new TreeSet<PassageRankingComponent.WindowTerm>();
    SortedSet<PassageRankingComponent.WindowTerm> secFollowTerms = new TreeSet<PassageRankingComponent.WindowTerm>();
    SortedSet<PassageRankingComponent.WindowTerm> bigrams = new TreeSet<PassageRankingComponent.WindowTerm>();

    Passage() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Passage result = (Passage) super.clone();
        result.terms = new TreeSet<>();
        for (PassageRankingComponent.WindowTerm term : terms) {
            result.terms.add((PassageRankingComponent.WindowTerm) term.clone());
        }
        result.prevTerms = new TreeSet<>();
        for (PassageRankingComponent.WindowTerm term : prevTerms) {
            result.prevTerms.add((PassageRankingComponent.WindowTerm) term.clone());
        }
        result.followTerms = new TreeSet<>();
        for (PassageRankingComponent.WindowTerm term : followTerms) {
            result.followTerms.add((PassageRankingComponent.WindowTerm) term.clone());
        }
        result.secPrevTerms = new TreeSet<>();
        for (PassageRankingComponent.WindowTerm term : secPrevTerms) {
            result.secPrevTerms.add((PassageRankingComponent.WindowTerm) term.clone());
        }
        result.secFollowTerms = new TreeSet<>();
        for (PassageRankingComponent.WindowTerm term : secFollowTerms) {
            result.secFollowTerms.add((PassageRankingComponent.WindowTerm) term.clone());
        }
        result.bigrams = new TreeSet<>();
        for (PassageRankingComponent.WindowTerm term : bigrams) {
            result.bigrams.add((PassageRankingComponent.WindowTerm) term.clone());
        }

        return result;
    }


    public void clear() {
        terms.clear();
        prevTerms.clear();
        followTerms.clear();
        secPrevTerms.clear();
        secPrevTerms.clear();
        bigrams.clear();
    }
}