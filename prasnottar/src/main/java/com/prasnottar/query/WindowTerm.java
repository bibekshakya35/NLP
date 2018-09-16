package com.prasnottar.query;

public class WindowTerm implements Cloneable, Comparable<WindowTerm> {
    String term;
    int position;
    int start, end = -1;

    WindowTerm(String term, int position, int startOffset, int endOffset) {
        this.term = term;
        this.position = position;
        this.start = startOffset;
        this.end = endOffset;
    }

    public WindowTerm(String term, int position) {
        this.term = term;
        this.position = position;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(WindowTerm o) {
        int result = position - o.position;
        if (result == 0) {
            result = term.compareTo(o.term);
        }
        return result;
    }

    @Override
    public String toString() {
        return "WindowTerm{" +
                "term='" + term + '\'' +
                ", position=" + position +
                '}';
    }
}
