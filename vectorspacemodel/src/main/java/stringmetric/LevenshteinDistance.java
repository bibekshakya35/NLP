package stringmetric;

/**
 * @author bibek on 1/14/18
 * @project vectorspacemodel
 * Levenshtein Edit Distance implementation
 */
public class LevenshteinDistance {
    public static int editDistance(String left, String right) {
        left = left.toLowerCase();
        right = right.toLowerCase();
        int[] costs = new int[right.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= left.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= right.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), left.charAt(i - 1) == right.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[right.length()];
    }
}
