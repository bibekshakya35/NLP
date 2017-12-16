package wordanalysis;

import com.aliasi.test.unit.tokenizer.IndoEuropean;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.StopTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

import java.util.HashSet;
import java.util.Set;

public class RunStopTokenizerFactory {
    public static void main(String[] args){
        TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
        tokenizerFactory = new LowerCaseTokenizerFactory(tokenizerFactory);
        Set<String> stopWords = new HashSet<>();
        stopWords.add("the");
        stopWords.add("of");
        stopWords.add("to");
        stopWords.add("is");
        tokenizerFactory = new StopTokenizerFactory(tokenizerFactory,stopWords);
    }

}
