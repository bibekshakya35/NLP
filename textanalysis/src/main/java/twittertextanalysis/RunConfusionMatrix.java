package twittertextanalysis;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.aliasi.classify.BaseClassifier;
import com.aliasi.classify.BaseClassifierEvaluator;
import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.util.AbstractExternalizable;

/**
 * Created by bibek on 9/6/17.
 */
public class RunConfusionMatrix {
    private static final Logger LOG = Logger.getLogger(ReadClassificationRunOnCsv.class.getName());

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String inputPath = args.length > 0 ? args[0] : "data/disney_e_n.csv";
        String classifierPath = args.length > 1 ? args[1] : "models/1LangId.LMClassifier";
        @SuppressWarnings("unchecked")
        BaseClassifier<CharSequence> classifier = (BaseClassifier<CharSequence>)
                AbstractExternalizable.readObject(new File(classifierPath));
        List<String[]> rows = Util.readAnnotatedCsvRemoveHeader(new File(inputPath));
        String[] categories = Util.getCategories(rows);
        boolean storeInputs = false;
        BaseClassifierEvaluator<CharSequence> evaluator
                = new BaseClassifierEvaluator<CharSequence>(classifier, categories, storeInputs);
        for (String[] row : rows) {
            String truth = row[Util.ANNOTATION_OFFSET];
            String text = row[Util.TEXT_OFFSET];
            Classification classification = new Classification(truth);
            Classified<CharSequence> classified = new Classified<>(text, classification);
            evaluator.handle(classified);
        }
        Util.printConfusionMatrix(evaluator.confusionMatrix());
    }
}
