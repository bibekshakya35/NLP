package nlp.qa;

import nlp.lucene.index.TermVectorOffsetInfo;
import nlp.solr.NameFilter;
//Not thread-safe, but should be lightweight to build

/**
 * The PassageRankingTVM is a Lucene TermVectorMapper that builds a five different windows around a matching term.
 * This Window can then be used to rank the passages
 */

class WindowBuildingTVM {
    //spanStart and spanEnd are the start and positions of where the match occurred in the document
    //from these values, we can calculate the windows
    int spanStart, spanEnd;
    Passage passage;
    private int primaryWS, adjWS, secWS;

    public WindowBuildingTVM(int primaryWindowSize, int adjacentWindowSize, int secondaryWindowSize) {
        this.primaryWS = primaryWindowSize;
        this.adjWS = adjacentWindowSize;
        this.secWS = secondaryWindowSize;
        passage = new Passage();//reuse the passage, since it will be cloned if it makes it onto the priority queue
    }



    public void map(String term, int frequency, TermVectorOffsetInfo[] offsets, int[] positions) {
        if (positions.length > 0 && term.startsWith(NameFilter.NE_PREFIX) == false && term.startsWith(NE_PREFIX_LOWER) == false) {//filter out the types, as we don't need them here
            //construct the windows, which means we need a bunch of bracketing variables to know what window we are in
            //start and end of the primary window
            int primStart = spanStart - primaryWS;
            int primEnd = spanEnd + primaryWS;
            // stores the start and end of the adjacent previous and following
            int adjLBStart = primStart - adjWS;
            int adjLBEnd = primStart - 1;//don't overlap
            int adjUBStart = primEnd + 1;//don't o
            int adjUBEnd = primEnd + adjWS;
            //stores the start and end of the secondary previous and the secondary following
            int secLBStart = adjLBStart - secWS;
            int secLBEnd = adjLBStart - 1; //don't overlap the adjacent window
            int secUBStart = adjUBEnd + 1;
            int secUBEnd = adjUBEnd + secWS;
            PassageRankingComponent.WindowTerm lastWT = null;
            for (int i = 0; i < positions.length; i++) {//unfortunately, we still have to loop over the positions
                //we'll make this inclusive of the boundaries, do an upfront check here so we can skip over anything that is outside of all windows
                if (positions[i] >= secLBStart && positions[i] <= secUBEnd) {
                    //fill in the windows
                    PassageRankingComponent.WindowTerm wt;
                    //offsets aren't required, but they are nice to have
                    if (offsets != null) {
                        wt = new PassageRankingComponent.WindowTerm(term, positions[i], offsets[i].getStartOffset(), offsets[i].getEndOffset());
                    } else {
                        wt = new PassageRankingComponent.WindowTerm(term, positions[i]);
                    }
                    if (positions[i] >= primStart && positions[i] <= primEnd) {//are we in the primary window
                        passage.terms.add(wt);
                        //we are only going to keep bigrams for the primary window.  You could do it for the other windows, too
                        if (lastWT != null) {
                            PassageRankingComponent.WindowTerm bigramWT = new PassageRankingComponent.WindowTerm(lastWT.term + "," + term, lastWT.position);//we don't care about offsets for bigrams
                            passage.bigrams.add(bigramWT);
                        }
                        lastWT = wt;
                    } else if (positions[i] >= secLBStart && positions[i] <= secLBEnd) {//are we in the secondary previous window?
                        passage.secPrevTerms.add(wt);
                    } else if (positions[i] >= secUBStart && positions[i] <= secUBEnd) {//are we in the secondary following window?
                        passage.secFollowTerms.add(wt);
                    } else if (positions[i] >= adjLBStart && positions[i] <= adjLBEnd) {//are we in the adjacent previous window?
                        passage.prevTerms.add(wt);
                    } else if (positions[i] >= adjUBStart && positions[i] <= adjUBEnd) {//are we in the adjacent following window?
                        passage.followTerms.add(wt);
                    }
                }
            }
        }
    }
}
