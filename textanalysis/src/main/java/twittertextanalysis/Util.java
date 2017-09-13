package twittertextanalysis;

import com.aliasi.classify.BaseClassifier;
import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.ConfusionMatrix;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.corpus.XValidatingObjectCorpus;
import com.aliasi.util.Strings;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.IOException;
import java.util.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * Created by bibek on 9/4/17.
 */
public class Util {
    private final static Logger LOG = Logger.getLogger(Util.class.getName());
    public static String SCORE_LABEL = "SCORE";
    public static String GUESS_LABEL = "GUESS";
    public static String TRUTH_LABEL = "TRUTH";
    public static String TEXT_LABEL = "TEXT";
    public static int ANNOTATION_OFFSET = 2;
    public static String[] ANNOTATION_HEADER_ROW = {SCORE_LABEL, GUESS_LABEL, TRUTH_LABEL, TEXT_LABEL};
    public static int ROW_LENGTH = ANNOTATION_HEADER_ROW.length;
    public static int TEXT_OFFSET = 3;

    public static void consoleInputPrintClassification(BaseClassifier<CharSequence> classifier) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            LOG.info("\n Type a string to be classified. Empty string to quit.");
            String data = reader.readLine();
            if (data.equals("")) {
                return;
            }
            Classification classification
                    = classifier.classify(data);
            LOG.info(classification.toString());
        }
    }

    public static void writeCsvAddHeader(List<String[]> data, File file) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file), Strings.UTF8));
        csvWriter.writeNext(ANNOTATION_HEADER_ROW);
        csvWriter.writeAll(data);
        csvWriter.close();
    }

    public static List<String[]> readCsvRemoveHeader(File file) throws IOException {
        FileInputStream fileIn = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileIn, Strings.UTF8);
        CSVReader csvReader = new CSVReader(inputStreamReader);
        csvReader.readNext();//skip headers
        List<String[]> rows = new ArrayList<String[]>();
        String[] row;
        while ((row = csvReader.readNext()) != null) {
            if (row[TEXT_OFFSET] == null || row[TEXT_OFFSET].equals("")) {
                continue;
            }
            rows.add(row);
        }
        csvReader.close();
        return rows;
    }

    /**
     * A method to provide a data which have annotated data in row
     *
     * @param file provide a file {@link File}
     * @return list of String []
     * @throws IOException can throw IOException
     */
    public static List<String[]> readAnnotatedCsvRemoveHeader(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Strings.UTF8);
        CSVReader csvReader = new CSVReader(inputStreamReader);
        csvReader.readNext();//skip headers
        List<String[]> rows = new ArrayList<String[]>();
        String[] row;

        while ((row = csvReader.readNext()) != null) {

            if (row[ANNOTATION_OFFSET].equals("")) {
                continue;
            }
            rows.add(row);
        }
        csvReader.close();
        return rows;
    }

    /**
     * get list of category involve in classification
     *
     * @param data read from csv liked file
     * @return array of unique category
     */
    public static String[] getCategories(List<String[]> data) {
        Set<String> categories = new HashSet<String>();
        for (String[] csvData : data) {
            if (!csvData[ANNOTATION_OFFSET].equals("")) {
                categories.add(csvData[ANNOTATION_OFFSET]);
            }
        }
        return categories.toArray(new String[0]);
    }

    public static String confusionMatrixToString(ConfusionMatrix confusionMatrix) {
        StringBuilder sb = new StringBuilder();
        String[] labels = confusionMatrix.categories();
        int[][] outcomes = confusionMatrix.matrix();
        sb.append("reference\\response\n");
        sb.append("          \\");
        for (String category : labels) {
            sb.append(category + ",");
        }
        for (int i = 0; i < outcomes.length; ++i) {
            sb.append("\n         " + labels[i] + " ");
            for (int j = 0; j < outcomes[i].length; ++j) {
                sb.append(outcomes[i][j] + ",");
            }
        }
        return sb.toString();
    }

    public static void printConfusionMatrix(ConfusionMatrix confMatrix) {
        LOG.info(confusionMatrixToString(confMatrix));
    }

    /**
     * to train and evaluate with cross validation
     *
     * @param rows
     * @param numFolds
     * @return
     */
    public static XValidatingObjectCorpus<Classified<CharSequence>> loadXValCorpus(List<String[]> rows, int numFolds) {
        XValidatingObjectCorpus<Classified<CharSequence>> corpus
                = new XValidatingObjectCorpus<>(numFolds);
        for (String[] row : rows) {
            String annotation = row[ANNOTATION_OFFSET];
            if (annotation.equals("")) {
                continue;
            }
            Classification classification = new Classification(annotation);
            Classified<CharSequence> classified = new Classified<>(row[TEXT_OFFSET], classification);
            corpus.handle(classified);
        }
        return corpus;
    }
    public static ObjectHandler<Classified<CharSequence>> corpusPrinter () {
        return new ObjectHandler<Classified<CharSequence>>() {
            @Override
            public void handle(Classified<CharSequence> e) {
                System.out.println(e.toString());
            }
        };
    }
}
