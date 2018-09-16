package twittertextanalysis;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramBoundaryLM;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bibek on 9/9/17.
 */
public class TrainAndRunLMClassifier {
    private static final Logger LOG = Logger.getLogger(TrainAndRunLMClassifier.class.getName());
    public static void main(String[] args) throws IOException {
        //if there is program argument else take default constant
        String dataPath = args.length > 0 ? args[0] : "data/disney_e_n.csv";
        List<String[]> annotatedData = Util.readAnnotatedCsvRemoveHeader(new File(dataPath));
        String[] categories = Util.getCategories(annotatedData);
        //get the category of language from file
        LOG.log(Level.INFO,"Here is the category {0}",new Object[]{
                Arrays.toString(categories)
        });
        int maxCharNGram = 3;
        DynamicLMClassifier<NGramBoundaryLM> classifier
                = DynamicLMClassifier.createNGramBoundary(categories,maxCharNGram);
        for (String[] row: annotatedData) {
            String truth = row[Util.ANNOTATION_OFFSET];
            String text = row[Util.TEXT_OFFSET];
            Classification classification = new Classification(truth);
            Classified<CharSequence> classified = new Classified<CharSequence>(text,classification);
            classifier.handle(classified);
            //int count = 1;
            //classifier.train(truth,text,count);
        }
        Util.consoleInputPrintClassification(classifier);
    }
}
