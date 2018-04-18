package fuzzystring;

/**
 * Jaccard measure or similarity coefficient is approach to capturing
 * the intuition that string that share more of the same characters are more similar.
 */
public class JaccardMeasure {
    public float jaccard(char[] s, char[] t) {
        int intersection = 0;
        int union = s.length + t.length;
        boolean[] sdup = new boolean[s.length];
        union -= findDuplicates(t, sdup);
        boolean[] tdup = new boolean[t.length];
        union -= findDuplicates(t, tdup);
        for (int si = 0; si < s.length; si++) {
            if (!sdup[si]) {
                for (int ti = 0; ti < t.length; ti++) {
                    if (s[si] == t[ti]) {
                        intersection++;
                        break;
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
                        sdup[si2] = s[si] == s[si2];
                    }
                }
            }
        }
        return ndup;
    }
}