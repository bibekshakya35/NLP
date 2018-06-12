package spec;

import org.junit.Assert;
import org.junit.BeforeClass;

import java.io.File;

public class TamingTextTestJ4  extends Assert{
    @BeforeClass
    public static void setUp(){
        File models = new File("/home/bibek/bs-workspace/tamingtext_resources/opennlp-models");
        assertTrue(models.exists());
        File wordnet = new File("/home/bibek/bs-workspace/tamingtext_resources/WordNet-3.0");
        assertTrue(wordnet.exists());
        System.setProperty("model.dir", "/home/bibek/bs-workspace/tamingtext_resources/opennlp-models");
        System.setProperty("wordnet.dir", "/home/bibek/bs-workspace/tamingtext_resources/WordNet-3.0");
    }
    public static File getWordNetDir(){
        String wordnetDir = System.getProperty("wordnet.dir");

        return new File(wordnetDir);
    }

    public static File getWordNetDictionary(){
        return new File(getWordNetDir(), "dict");
    }

    public static File getModelDir(){
        String modelsDirProp = System.getProperty("model.dir");

        return new File(modelsDirProp);
    }
    public static File getPersonModel(){
        return new File(getModelDir(), "en-ner-person.bin");
    }
}
