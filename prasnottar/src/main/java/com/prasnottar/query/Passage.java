package com.prasnottar.query;

import java.util.SortedSet;
import java.util.TreeSet;

public class Passage implements Cloneable {
    int lDocId;
    String field;
    float score;
    SortedSet<WindowTerm> terms = new TreeSet<>();
    SortedSet<WindowTerm> prevTerms = new TreeSet<>();
    SortedSet<WindowTerm> followTerms = new TreeSet<>();
    SortedSet<WindowTerm> secPrevTerms = new TreeSet<>();
    SortedSet<WindowTerm> secFollowTerms = new TreeSet<>();
    SortedSet<WindowTerm> bigrams = new TreeSet<>();

    Passage() {
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Passage result = (Passage)super.clone();
        result.terms = new TreeSet<>();
        for (WindowTerm term : terms){
            result.terms.add((WindowTerm)term.clone());
        }
        result.prevTerms = new TreeSet<>();
        for (WindowTerm term : prevTerms){
            result.prevTerms.add((WindowTerm)term.clone());
        }
        result.followTerms =new TreeSet<>();
        for (WindowTerm term : followTerms){
            result.followTerms.add((WindowTerm)term.clone());
        }
        result.secPrevTerms = new TreeSet<>();
        for (WindowTerm term : secPrevTerms){
            result.secPrevTerms.add((WindowTerm)term.clone());
        }
        result.secFollowTerms = new TreeSet<>();
        for (WindowTerm term : secFollowTerms){
            result.secFollowTerms.add((WindowTerm)term.clone());
        }
        result.bigrams = new TreeSet<WindowTerm>();
        for (WindowTerm term : bigrams) {
            result.bigrams.add((WindowTerm) term.clone());
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
