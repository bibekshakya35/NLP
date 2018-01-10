package spec;

import org.junit.Test;
import sentencedetection.SentenceDetection;

import java.io.IOException;

/**
 * @author bibek on 1/10/18
 * @project tamingtext
 */
public class SentenceDetectionSpec {
    @Test
    public void detectSentenceFromOpenNLP() throws IOException {
        SentenceDetection sentenceDetection = new SentenceDetection();
        sentenceDetection.usingOpenNLPForSentenceDetection();
    }
}
