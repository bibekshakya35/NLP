package nlp.tokenization;

import nlp.common.CommonText;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * @author bibek on 1/7/18
 * @project tamingtext
 */
public class SimpleTokenizerONLP {
    public static void main(String[] args) throws FileNotFoundException{
        List<String> list = CommonText.listOfStringFromFile();
        //Intializing SimpleTokenizerONLP class
        opennlp.tools.tokenize.SimpleTokenizer simpleTokenizer =
                opennlp.tools.tokenize.SimpleTokenizer.INSTANCE;
        list.forEach(line->System.out.println(Arrays.toString(simpleTokenizer.tokenize(line))));
    }
}
