package fsm;

import org.junit.Test;

import java.util.BitSet;
import java.util.Vector;

public class TanimotoCoefficientSpec {
    @Test
    public void shouldProvideEqualFeature() {
        String featureOne = "Bibek";
        String featureTwo = "Bibek";
        Vector featureOneVector = new Vector();
        char[] sChars = featureOne.toCharArray();
        for (int i = 0; i <= sChars.length; i++) {
            featureOneVector.add(sChars[i]);
        }
        Vector featureTwoVector = new Vector();
        char[] f2Chars = featureTwo.toCharArray();
        for (int i = 0; i <= f2Chars.length; i++) {
            featureTwoVector.add(f2Chars[i]);
        }
    }

    @Test
    public void shouldProvideSimilarBitSet() throws IllegalAccessException {
        //float data = TanimotoCoefficient.calculate();
        //System.out.println(data);
    }
}
