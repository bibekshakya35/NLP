package fsm;

/**
 * Jaccard measure or similarity coefficient is one approach to capturing the intuition
 * that strings which share more of the same characters are more similar. In the context
 * of string comparisons, it’s computed as the percentage of unique characters that two
 * strings share when compared to the total number of unique characters in both strings.
 * More formally, where A is the set of characters in the first string, and B is the set of
 * characters in the second string, this can be expressed as follows:
 * A ∩ B
 * -----
 * A ∪ B
 */
public class JaccardMeasure {
    public float jaccard(char[] s, char[] t) {
        int intersection = 0;
        int union = s.length + t.length;
        boolean[] sdup = new boolean[s.length];
        union -= findDuplicates(s, sdup);
        boolean[] tdup = new boolean[t.length];
        union -= findDuplicates(t, tdup);
        for (int si = 0; si < s.length; si++) {
            if (!sdup[si]) {
                for (int ti = 0; ti < t.length; ti++) {
                    if (!tdup[ti]) {
                        if (s[si] == t[ti]) {
                            intersection++;
                            break;
                        }
                    }
                }
            }
        }
        union -= intersection;
        return (float) intersection / union;
    }

    private int findDuplicates(char[] s, boolean[] sdup) {
        int ndup = 0;
        for (int si = 0; si < s.length; si++) {
            if (sdup[si]) {
                ndup++;
            } else {
                for (int si2 = si + 1; si2 < s.length; si2++) {
                    if (!sdup[si2]) {
                        sdup[si2] = s[si] == si2;
                    }
                }
            }
        }
        return ndup;
    }
}
