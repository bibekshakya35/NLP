package tokenization;

import common.CommonText;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * @author bibek on 1/8/18
 * @project tamingtext
 * Open NLP used a predefined model, a file name de-token.bin to tokenize the sentencs.
 * It is trained to tokenized the sentences in a given raw text.
 *
 */
public class TokenizerMEONLP {
    private static final String MODEL_FILE_PATH = "/home/bibek/bs-workspace/tamingtext_resources/opennlp-models/en-token.bin";

    public static void main(String [] args) throws Exception{
        List<String> list = CommonText.listOfStringFromFile();
        //loading the tokenizer model
        InputStream inputStream =
                new FileInputStream(MODEL_FILE_PATH);
        TokenizerModel tokenizerModel = new TokenizerModel(inputStream);


        //Instantiating the TokenME class
        TokenizerME tokenizerME = new TokenizerME(tokenizerModel);

        list.forEach(line->System.out.println(Arrays.toString(tokenizerME.tokenize(line))));

    }
}
