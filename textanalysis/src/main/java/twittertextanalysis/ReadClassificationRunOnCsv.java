package twittertextanalysis;

import com.aliasi.classify.BaseClassifier;
import com.aliasi.classify.Classification;
import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bibek on 9/4/17.
 */
public class ReadClassificationRunOnCsv {
    private static final Logger LOG = Logger.getLogger(ReadClassificationRunOnCsv.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String inputPath = "data/twitterSearch.csv";
        String classifierPath = "models/3LangId.LMClassifier";
        BaseClassifier<CharSequence> classifier =
                (BaseClassifier<CharSequence>)
                        AbstractExternalizable.readObject(new File(classifierPath));
        List<String[]> lines = Util.readCsvRemoveHeader(new File(inputPath));
        lines.forEach(line -> {
            String text = line[Util.TEXT_OFFSET];
            Classification classified = classifier.classify(text);
            LOG.log(Level.INFO, "Input Text : {0}", new Object[]{text});
            LOG.log(Level.INFO, "Best Classified Language: {0}", new Object[]{classified.bestCategory()});
        });
    }
}
