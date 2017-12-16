package wordanalysis;

import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

public class TestTokenizerFactory {
    static void checkTokens(TokenizerFactory tokenizerFactory,String input,String[] correctTokens){
        Tokenizer tokenizer = tokenizerFactory.tokenizer(input.toCharArray(),0,input.length());
        String[] tokens = tokenizer.tokenize();
        
    }
}
