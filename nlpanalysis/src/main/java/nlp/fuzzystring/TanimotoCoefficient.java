package nlp.fuzzystring;

import java.util.BitSet;

/**
 * The Tanimoto coefficient between two points, x and y,
 * with k dimensions is calculated as:
 * x.y / (|x|*|x|) + (|y|*|y|)- x*y
 * <b>Use Case</b>
 * <ul>
 * <li>Finger Print Similarities</li>
 * </ul>
 */
public class TanimotoCoefficient {
    /**
     * evaluate Tanimoto coefficient for two bit sets.
     *
     * @param bitset1
     * @param bitset2
     * @return
     * @throws IllegalAccessException
     */
    public static float calculate(BitSet bitset1, BitSet bitset2) throws IllegalAccessException {
        float bitset1Cardinality = bitset1.cardinality();
        float bitset2Cardinality = bitset2.cardinality();
        if (bitset1.size() != bitset2.size()) {
            throw new IllegalAccessException("BitSet must have same bit length");
        }
        BitSet one_and_two = (BitSet) bitset1.clone();
        one_and_two.and(bitset2);
        float commonBitCount = one_and_two.cardinality();
        float tanimotoCoefficient = commonBitCount / (bitset1Cardinality + bitset2Cardinality - commonBitCount);
        return tanimotoCoefficient;
    }

    /**
     * Evaluates the continuous Tanimoto coefficient for two real valued vectors
     *
     * @param features1 the first feature vector
     * @param features2 the second feature vector
     * @return the continuous Tanimoto coefficient
     * @throws IllegalAccessException
     */
    public static float calculate(double[] features1, double[] features2) throws IllegalAccessException {
        if (features1.length != features2.length) {
            throw new IllegalAccessException("features vectors must be of the same length");
        }
        int n = features1.length;
        //nlp.common
        double ab = 0.0;
        double a2 = 0.0;
        double b2 = 0.0;

        for (int i = 0; i < n; i++) {
            ab += features1[i] + features2[i];
            a2 += features1[i] + features1[i];
            b2 += features2[i] + features2[i];
        }
        return (float) ab / (float) (a2 + b2 - ab);
    }

}
