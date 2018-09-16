package spec.solr;

import junit.framework.TestCase;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.junit.BeforeClass;
import org.junit.Test;
import nlp.solr.NameFilter;
import nlp.solr.SentenceTokenizer;
import spec.TamingTextTestJ4;

import java.io.*;

public class NameFilterTest extends TamingTextTestJ4 {
    private static final String input =
            "The quick brown fox jumped over William Taft the President. " +
                    "There once was a man from New York City who had to catch the bus at 10:30 " +
                    "in the morning of December 21, 1992 ";


    private static String[] modelName = {
            "date", "location", "money", "organization",
            "percentage", "person", "time"
    };
    private static SentenceDetector detector;
    private static NameFinderME[] nameFinderMES;

    @BeforeClass
    public static void setUpModels() throws IOException {
        File modelDir = getModelDir();
        nameFinderMES = new NameFinderME[modelName.length];
        for (int i = 0; i < modelName.length; i++) {
            nameFinderMES[i] = new NameFinderME(new TokenNameFinderModel(new FileInputStream(
                    new File(modelDir, "en-ner-" + modelName[i] + ".bin")
            )));
        }
        File modelFile = new File(modelDir, "en-sent.bin");
        InputStream modelStream = new FileInputStream(modelFile);
        SentenceModel sentenceModel = new SentenceModel(modelStream);
        detector = new SentenceDetectorME(sentenceModel);
    }

    String[] tokenStrings = {
            "The", "quick", "brown", "fox", "jumped", "over", "NE_person", "William",
            "NE_person", "Taft", "the", "President", ".", "There", "once", "was", "a",
            "man", "from", "NE_location", "New", "NE_location", "York", "NE_location", "City",
            "who", "had", "to", "catch", "the", "bus", "at", "NE_time", "10", "NE_time", ":",
            "NE_time", "30", "in", "the", "morning", "of", "NE_date", "December", "NE_date",
            "21", "NE_date", ",", "NE_date", "1992"
    };

    int[] positionIncrements = {
            1, 1, 1, 1, 1, 1, 1, 0,
            1, 0, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 0, 1, 0, 1, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0,
            1, 0, 1, 1, 1, 1, 1, 0, 1,
            0, 1, 0, 1, 0
    };

    @Test
    public void testNameFilter() throws IOException {
        Reader in = new StringReader(input);
        Tokenizer tokenizer = new SentenceTokenizer(in,detector);

        NameFilter nameFilter = new NameFilter(tokenizer, modelName, nameFinderMES);
        CharTermAttribute charTermAttribute;
        PositionIncrementAttribute positionIncrementAttribute;
        OffsetAttribute offsetAttribute;
        int pass = 0;
        while (pass < 2) {
            int pos = 0;
            int lastStart = 0;
            int lastEnd = 0;
            while (nameFilter.incrementToken()) {
                charTermAttribute = (CharTermAttribute) nameFilter.getAttribute(CharTermAttribute.class);
                positionIncrementAttribute = (PositionIncrementAttribute)
                        nameFilter.getAttribute(PositionIncrementAttribute.class);
                offsetAttribute = (OffsetAttribute) nameFilter.getAttribute(OffsetAttribute.class);
                System.out.println("CharTermAttribute : " + charTermAttribute.toString());
                System.out.println("PositionIncrementAttribute : "+positionIncrementAttribute.toString());
                System.out.println("OffsetAttribute "+offsetAttribute.toString());
                System.err.println("--- pass: " + pass);
                TestCase.assertEquals(tokenStrings[pos], charTermAttribute.toString());
                TestCase.assertEquals(positionIncrements[pos], positionIncrementAttribute.getPositionIncrement());
                if (positionIncrementAttribute.getPositionIncrement() == 0) {
                    TestCase.assertEquals(lastStart, offsetAttribute.startOffset());
                    TestCase.assertEquals(lastEnd, offsetAttribute.endOffset());
                }
                if (!charTermAttribute.toString().startsWith("NE_")) {
                    TestCase.assertEquals(input.substring(offsetAttribute.startOffset(), offsetAttribute.endOffset()), charTermAttribute.toString());
                }
                lastStart = offsetAttribute.startOffset();
                lastEnd = offsetAttribute.endOffset();
                pos++;
            }
            nameFilter.end();
            in.close();
            in = new StringReader(input);
            tokenizer.reset();
            pass++;
        }
    }
}
