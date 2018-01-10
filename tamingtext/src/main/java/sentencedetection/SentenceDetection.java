package sentencedetection;

import common.CommonText;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.*;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author bibek on 1/10/18
 * @project tamingtext
 * Using {@link BreakIterator} for breaking sentence
 */
public class SentenceDetection {
    //the BreakIterator handled the inline etc, it didn’t properly handle Mr..
    public void usingBreakIterator(){
        BreakIterator sentenceIterator = BreakIterator.getSentenceInstance(Locale.US);
        String testStr = "This is a sentence. It has fruits, vegetables, etc." +
                "but does not have meat. Mr. Smith went to Washington.";
        sentenceIterator.setText(testStr);
        int start = sentenceIterator.first();
        int end = -1;
        List<String> sentences = new ArrayList<>();
        while ((end=sentenceIterator.next())!=BreakIterator.DONE){
            String sentence = testStr.substring(start,end);
            start = end;
            sentences.add(sentence);
            System.out.println(" Sentence : "+sentence);
        }
    }

    /**
     * Using {@link SentenceDetector} for sentence detection
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void usingOpenNLPForSentenceDetection() throws FileNotFoundException,IOException{
        File modelFile = new File("/home/bibek/bs-workspace/tamingtext_resources/opennlp-models/en-sent.bin");
        InputStream modelStream = new FileInputStream(modelFile);
        SentenceModel model =new SentenceModel(modelStream);
        SentenceDetector sentenceDetector = new SentenceDetectorME(model);
        List<String> data = CommonText.provideSentenceForDetection();
        List<String[]> results = new ArrayList<>();
        data.forEach(temp ->results.add(sentenceDetector.sentDetect(temp)));
        results.forEach(res->{
            for (String tem : res){
                System.out.println("Sentence : "+tem);
            }
        });
    }

    private String getModelDir() {
        return "/home/bibek/bs-workspace/tamingtext_resources/opennlp-models";
    }
}
