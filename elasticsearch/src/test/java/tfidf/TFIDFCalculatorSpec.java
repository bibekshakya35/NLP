package tfidf;

import org.junit.Test;

/**
 * @author bibek on 12/19/17
 * @project elasticsearch
 */
public class TFIDFCalculatorSpec {
    @Test
    public void occurrence(){
        TFIDFCalculator t = new TFIDFCalculator();
        t.countWordOccurence();
    }
}
