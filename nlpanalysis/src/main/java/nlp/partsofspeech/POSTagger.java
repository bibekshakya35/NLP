package nlp.partsofspeech;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import nlp.common.CommonText;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bibek on 1/8/18
 * @project tamingtext
 */
public class POSTagger {
    public static void main(String[] args) throws IOException{
        File posModelFile = new File(getModelDir(),"en-pos-maxent.bin");
        FileInputStream posModelStream = new FileInputStream(posModelFile);
        POSModel posModel = new POSModel(posModelStream);
        POSTaggerME taggerME = new POSTaggerME(posModel);
        List<String> list = CommonText.listOfStringFromFile();
        List<String[]> words = new ArrayList<>();
        list.forEach(line->words.add(SimpleTokenizer.INSTANCE.tokenize(line)));
        List<String[]> results = new ArrayList<>();
        words.forEach(temp->results.add(taggerME.tag(temp)));
        for (int i = 0; i < words.size(); i++) {
            String[] wordsArr = words.get(i);
            String[] posArr = results.get(i);
            for (int j = 0; j <wordsArr.length; j++) {
                System.out.println(wordsArr[j]+ " / "+posArr[j]);
            }

        }
    }

    public static String getModelDir() {
        return "/home/bibek/bs-workspace/tamingtext_resources/opennlp-models/";
    }
}
