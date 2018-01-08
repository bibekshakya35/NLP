package tokenization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author bibek on 1/7/18
 * @project tamingtext
 */
public class SimpleTokenizerONLP {
    public static void main(String[] args) throws FileNotFoundException{
        List<String> list = CommonTokenization.listOfStringFromFile();
        //Intializing SimpleTokenizerONLP class
        opennlp.tools.tokenize.SimpleTokenizer simpleTokenizer =
                opennlp.tools.tokenize.SimpleTokenizer.INSTANCE;
        list.forEach(line->System.out.println(Arrays.toString(simpleTokenizer.tokenize(line))));
    }
}
