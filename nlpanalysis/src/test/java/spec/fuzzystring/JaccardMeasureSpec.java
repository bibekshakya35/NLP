package spec.fuzzystring;

import nlp.fuzzystring.JaccardMeasure;
import org.junit.Assert;
import org.junit.Test;

public class JaccardMeasureSpec {
    //delta - the maximum delta between expected and actual
    // for which both numbers are still considered equal.
    private static final double DELTA = 1e-15;

    @Test
    public void measurement() {
        String firstCase = "hello";
        String secondCase = "hallo";
        String thirdCase = "hello";
        JaccardMeasure jaccardMeasure = new JaccardMeasure();
        Assert.assertEquals("Error on measurement for case 1 and 2",
                jaccardMeasure.
                        jaccard(firstCase.toCharArray(), secondCase.toCharArray()),
                0.6666666865348816, DELTA);
        Assert.assertEquals("Error on measurement for case 1 and 3",
                jaccardMeasure.
                        jaccard(firstCase.toCharArray(),
                                thirdCase.toCharArray()),
                1, DELTA);
    }
}
