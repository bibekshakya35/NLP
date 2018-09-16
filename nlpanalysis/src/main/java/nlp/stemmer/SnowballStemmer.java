package nlp.stemmer;

import org.tartarus.snowball.ext.EnglishStemmer;

/**
 * @author bibek on 1/8/18
 * @project tamingtext
 */
public class SnowballStemmer {
    public static void main(String[] args){
        EnglishStemmer stemmer = new EnglishStemmer();
        String[] test = {"bank", "banks", "banking", "banker", "banked","bankers"};
        String[] gold = {"bank", "bank", "bank", "banker", "bank", "banker"};
        for (int i = 0;i<test.length;i++){
            stemmer.setCurrent(test[i]);
            stemmer.stem();
            System.out.println("English "+stemmer.getCurrent());
            assert stemmer.getCurrent().equals(gold[i]);
        }
    }
}
