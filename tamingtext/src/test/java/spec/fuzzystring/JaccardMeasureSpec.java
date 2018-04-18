package spec.fuzzystring;

import fuzzystring.JaccardMeasure;
import org.junit.Assert;
import org.junit.Test;

public class JaccardMeasureSpec {
    @Test
    public void shouldMeasure() {
        JaccardMeasure jaccardMeasure = new JaccardMeasure();
        float measure = jaccardMeasure.jaccard("hello".toCharArray(), "hello".toCharArray());
        Assert.assertEquals(1F, measure, 1000F);
        float measure1 = jaccardMeasure.jaccard("bibek".toCharArray(), "babek".toCharArray());
        Assert.assertEquals(0.6, measure1, 10000F);
    }
}
