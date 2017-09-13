package wordanalysis;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by bibek on 9/9/17.
 */
public class RunBaseTokenizerFactory {
    /**
     * This method will get input from console
     * evaluate the word and provide token of word and whitespace
     * @throws IOException in case of console error
     */
    public void doTokenization() throws IOException {
        TokenizerFactory tokFactory = IndoEuropeanTokenizerFactory.INSTANCE;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("type a sentence to see to see tokens and white spaces");
            String input = reader.readLine();
            if(input.isEmpty()){
                System.err.println("Her vai kehi hal");
            }
            Tokenizer tokenizer = tokFactory.tokenizer(input.toCharArray(), 0, input.length());
            String token = null;
            while ((token = tokenizer.nextToken()) != null) {
                System.out.println("Token:'" + token + "'");
                System.out.println("WhiteSpace:'" + tokenizer.nextWhitespace() + "'");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new RunBaseTokenizerFactory().doTokenization();
    }
}
