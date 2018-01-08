package tokenization;

import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 * @author bibek on 1/7/18
 * @project tamingtext
 */
public class WhiteSpaceTokenizerONLP {
    public static void main(String[] args) throws FileNotFoundException{
        List<String> list =CommonTokenization.listOfStringFromFile();
        WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;
        list.forEach(line->System.out.println(Arrays.toString(whitespaceTokenizer.tokenize(line))));
    }
}
