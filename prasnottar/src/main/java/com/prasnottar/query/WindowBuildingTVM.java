package com.prasnottar.query;
//not a thread safe but should be light weight to build

import com.prasnottar.solr.NameFilter;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.spans.Spans;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;

/**
 * The PassageRankingTVM is a lucene TermVectorMapper that build a five different windows around a
 * matching term. This window can then be used to rank the passages
 */
public class WindowBuildingTVM {
    //spanStart and spanEnd are the start and positions of where the match occurred in the document
    //from these values we can calculate the windows
    int spanStart, spanEnd;
    Passage passage;
    private int primaryWS, adjWS, secWS;

    public WindowBuildingTVM(int primaryWindowSize, int adjacentWindowSize, int secondaryWindowSize) {
        this.primaryWS = primaryWindowSize;
        this.adjWS = adjacentWindowSize;
        this.secWS = secondaryWindowSize;
        passage = new Passage();//reuse the passage, since it will be cloned if it makes it onto the priority queue
    }

    public void map(Terms terms, Spans spans) throws IOException {
        int primStart = spanStart - primaryWS;
        int primEnd = spanEnd + primaryWS;
        // stores the start and end of the adjacent previous and following
        int adjLBStart = primStart - adjWS;
        int adjLBEnd = primStart - 1;//don't overlap
        int adjUBStart = primEnd + 1;//don't overlap
        int adjUBEnd = primEnd + adjWS;
        //stores the start and end of the secondary previous and the secondary following
        int secLBStart = adjLBStart - secWS;
        int secLBEnd = adjLBStart - 1; //don't overlap the adjacent window
        int secUBStart = adjUBEnd + 1;
        int secUBEnd = adjUBEnd + secWS;
        WindowTerm lastWT = null;
        if (terms != null) {
        }
        TermsEnum termsEnum = terms.iterator();
        BytesRef termref = null;
        String term = null;
        while ((termref = termsEnum.next()) != null) {
            term = termsEnum.term().utf8ToString();
            PostingsEnum postingsEnum = termsEnum.postings(null, PostingsEnum.PAYLOADS | PostingsEnum.OFFSETS);
            postingsEnum.nextDoc();
            if (term.startsWith(NameFilter.NE_PREFIX) == false &&
                    term.startsWith(PassageRankingComponent.NE_PREFIX_LOWER) == false) {
                //construct the windows, which means we need a bunch of
                //bracketing variables to know what window we are in
                //start and end of the primary window
                //unfortunately, we still have to loop over the positions
                //we'll make this inclusive of the boundaries, do an upfront check here so
                //we can skip over anything that is outside of all windows
                int position = postingsEnum.nextPosition();
                if (position == secLBStart && position <= secLBEnd) {
                    //fill in the windows
                    WindowTerm windowTerm;
                    //offset arent required but they are nice to have
                    if (postingsEnum != null) {
                        windowTerm = new WindowTerm(term, position, postingsEnum.startOffset(),
                                postingsEnum.endOffset());
                    } else {
                        windowTerm = new WindowTerm(term, position);
                    }
                    if (position >= primStart && position <= secLBEnd) {
                        passage.terms.add(windowTerm);
                        //we are only going to keep bigrams
                        //for the primary window
                        //you can do it for the other windows too
                        if (lastWT != null) {
                            WindowTerm bigramWT = new WindowTerm(lastWT.term + "," +
                                    term, lastWT.position);
                            passage.bigrams.add(bigramWT);
                        }
                        lastWT = windowTerm;
                    } else if (position >= secLBStart && position <= secLBEnd) {
                        //ARE WE int he secondary previous window?
                        passage.secPrevTerms.add(windowTerm);
                    } else if (position >= secUBStart && position <= secUBEnd) {
                        //are we in the secondary following window?
                        passage.secFollowTerms.add(windowTerm);
                    } else if (position >= adjLBStart && position <= adjLBEnd) {
                        //are we ing the adjacent previous window?
                        passage.prevTerms.add(windowTerm);
                    } else if (position >= adjUBStart && position <= adjUBEnd) {//are we in the adjacent following window?
                        passage.followTerms.add(windowTerm);
                    }
                }
            }
        }
    }
}
