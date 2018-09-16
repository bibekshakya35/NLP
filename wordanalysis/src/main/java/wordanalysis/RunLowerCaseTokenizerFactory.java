package wordanalysis;

import com.aliasi.tokenizer.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by bibek on 9/9/17.
 */
public class RunLowerCaseTokenizerFactory {
    /**
     * provide lowercase token
     * @throws IOException
     */
    public void doLowerCaseTokenizer() throws IOException {
        TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
        tokenizerFactory = new LowerCaseTokenizerFactory(tokenizerFactory);
        tokenizerFactory = new WhitespaceNormTokenizerFactory(tokenizerFactory);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("type a sentence below to see the tokens and white spaces are:");
            String input = reader.readLine();
            Tokenizer tokenizer = tokenizerFactory.tokenizer(input.toCharArray(), 0, input.length());
            String token = null;
            while ((token = tokenizer.nextToken()) != null) {
                System.out.println("Token:'" + token + "'");
                System.out.println("WhiteSpace:'" + tokenizer.nextWhitespace() + "'");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new RunLowerCaseTokenizerFactory().doLowerCaseTokenizer();
    }
}
