package nlp.chapterone;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bibek on 12/11/17
 * @project tamingtext
 * Parse Frankenstein book (located in resource folder)
 * identifies sentences and then indexes them in Lucene.
 */
public class Frankenstein {
    protected RAMDirectory directory;
    protected StandardAnalyzer analyzer;
    protected IndexSearcher searcher;
    protected SentenceDetector sentenceDetector;
    protected Map<String, NameFinderME> finders;
    protected Tokenizer tokenizer;

    public static void main(String[] args) throws IOException, ParseException {
        Frankenstein frankenstein = new Frankenstein();
        frankenstein.init();
        frankenstein.index();
        String query = null;

        while (true) {
            query = getQuery();
            Results results = frankenstein.search(query);
            frankenstein.examineResult(results);
            displayResult(results);
        }
    }

    private void examineResult(Results results) {
        results.getMatches().forEach(d -> {
            //we have a paragraph
            //lets break into nlp.sentence to applY NER
            String[] sentencesStr = sentenceDetector.sentDetect(d.get("paragraph"));
            if (sentencesStr != null && sentencesStr.length > 0) {
                Sentence[] sentences = new Sentence[sentencesStr.length];
                results.putSentences(d.get("id"), sentences);
                for (int i = 0; i < sentencesStr.length; i++) {
                    sentences[i] = new Sentence(sentencesStr[i]);
                    String[] tokens = tokenizer.tokenize(sentencesStr[i]);
                    for (Map.Entry<String, NameFinderME> finder : finders.entrySet()) {
                        String label = finder.getKey();
                        Span[] names = finder.getValue().find(tokens);
                        //span index into tokens array
                        if (names != null && names.length > 0) {
                            List<String> values = new ArrayList<>();
                            for (int j = 0; j < names.length; j++) {
                                StringBuffer cb = new StringBuffer();
                                for (int t1 = names[j].getStart(); t1 < names[j].getEnd(); t1++) {
                                    cb.append(tokens[t1]).append(" ");
                                }
                                values.add(cb.toString());
                            }
                            sentences[i].putSentence(label,values);
                        }

                    }
                }
            }
        });
    }
    private static void displayResult(Results results){

        results.getMatches().forEach(document->{
            int k = 0;
            System.out.println("-----------------------------------");
            System.out.println("Match: [" + k + "] Paragraph: " + document.get("paragraphNumber"));
            System.out.println("Lines: " + document.get("startLine") + "-" + document.get("finishLine"));
            System.out.println("\t" + document.get("paragraph"));
            System.out.println("\t----- Sentences ----");
            Sentence[] sentences = results.getSentences().get(document.get("id"));
            for (int i = 0; i <sentences.length; i++) {
                Sentence sentence = sentences[i];
                System.out.println("\t\t[" + i + "] " + sentence.getSentence());
                if (!sentence.getNames().isEmpty()){
                    for (Map.Entry<String,List<String>> entry : sentence.getNames().entrySet()){
                        final StringBuffer buff = new StringBuffer();
                        buff.append(entry.getKey()).append(" : ");
                        if (!entry.getValue().isEmpty()){
                            entry.getValue().forEach(e->{
                                buff.append(e.trim()).append(", ");
                            });
                            //Drop the comma and space
                            buff.setLength(buff.length()-2);
                            System.out.println("\t\t\t"+buff);
                        }
                    }
                    System.out.println("");
                }

            }
            k++;
        });
    }

    private Results search(String queryStr) throws ParseException, IOException {
        System.out.println("Searching for : " + queryStr);
        if (searcher == null) {
            try {
                DirectoryReader dr = DirectoryReader.open(directory);
                searcher = new IndexSearcher(dr);
            } catch (IOException io) {
                System.err.println(io.getMessage());
            }
        }
        Results results = new Results();
        QueryParser queryParser = new QueryParser("paragraph",analyzer);
        Query query = queryParser.parse(queryStr);
        TopDocs topDocs = searcher.search(query, 20);
        System.out.println("Found " + topDocs.totalHits + " total hits.");
        for (int i = 0; i < topDocs.scoreDocs.length; i++) {
            Document hitDoc = searcher.doc(topDocs.scoreDocs[i].doc);
            results.getMatches().add(hitDoc);
        }
        return results;
    }

    private static String getQuery() throws IOException {
        System.out.println("");
        System.out.println("Type your query.  Hit Enter to process the query (the empty string will exit the program):");
        System.out.print('>');
        System.out.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
        String line = in.readLine();
        if (line == null || line.length() == -1 || line.equals("")) {
            return null;
        }
        return line;
    }

    /**
     * Index content of frakenstein
     */
    private void index() throws IOException {
        System.out.println("Indexing frankenstein");
        InputStream stream = getClass()
                .getClassLoader()
                .getResourceAsStream("frankenstein-gutenberg.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        directory = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        String line;
        StringBuilder paraBuilder = new StringBuilder(2048);
        int lines = 0;
        int paragraphs = 0;
        int paragraphLines = 0;
        while ((line = reader.readLine()) != null) {
            //we are in the license section at the end of the book
            if (line.contains("End of the Project Gutenberg")) {
                break;
            }
            //skip comments
            if (line.startsWith("#")) {
                continue;
            }
            //if the line is blank and we have a paragraph. so let's index it
            if (line.matches("^\\s*$") && paraBuilder.length() > 0) {
                Document document = new Document();
                //we can retrieve by paragraph number if we want
                String paragraphStr = paraBuilder.toString();
                paragraphStr.trim();
                if (paragraphStr.length() > 0 && paragraphStr.matches("^\\s*") == false) {
                    addMetaData(document, lines, paragraphs, paragraphLines);
                    document.add(new TextField("paragraph", paragraphStr, Field.Store.YES));
                    indexWriter.addDocument(document);
                    paragraphs++;
                }
                paraBuilder.setLength(0);
                paragraphLines = 0;
            } else {
                paraBuilder.append(line).append(' ');
            }
            lines++;
            paragraphLines++;
        }
        System.out.println("Processed " + lines + " lines. Paragraphs : " + paragraphs);
        indexWriter.close();
    }

    private void addMetaData(Document document, int lines, int paragraphs, int paragraphLines) {
        StringField paragraphField = new StringField("id", "frank_" + paragraphs, Field.Store.YES);
        document.add(paragraphField);
        StringField startLine = new StringField("startLine", String.valueOf(lines - paragraphLines),Field.Store.YES);
        document.add(startLine);
        StringField finishLine = new StringField("finishLine", String.valueOf(lines),Field.Store.YES);
        document.add(finishLine);
        StringField paragraphNumber = new StringField("paragraphNumber", String.valueOf(paragraphs),Field.Store.YES);
        document.add(paragraphNumber);
    }

    /**
     * use opennlp-models
     * use WordNet-3.0
     * from opennlp-models load en-sent.bin for nlp.sentence detection :Trained on opennlp training data.
     *
     * @throws IOException
     */
    private void init() throws IOException {
        System.out.println("Initializing Frakenstein");
        analyzer = new StandardAnalyzer();
        File models = new File("/home/bibek/bs-workspace/tamingtext_resources/opennlp-models");
        File wordnet = new File("/home/bibek/bs-workspace/tamingtext_resources/WordNet-3.0");
        if (models.exists() == false) {
            throw new FileNotFoundException("./opennlp-models");
        }
        System.setProperty("model.dir", "/home/bibek/bs-workspace/tamingtext_resources/opennlp-models");
        System.setProperty("wordnet.dir", "/home/bibek/bs-workspace/tamingtext_resources/WordNet-3.0");
        File modelFile = new File(models, OpenNLPModel.SentenceDetector.getModelFileName());
        InputStream modelInStream = new FileInputStream(modelFile);
        SentenceModel sentenceModel = new SentenceModel(modelInStream);
        sentenceDetector = new SentenceDetectorME(sentenceModel);
        finders = new HashMap<>();
        InputStream personModelInputStream = new FileInputStream(getPersonModel());
        TokenNameFinderModel personTokenNameFinderModel = new TokenNameFinderModel(personModelInputStream);
        NameFinderME personNameFinderME = new NameFinderME(personTokenNameFinderModel);
        finders.put("Names", personNameFinderME);
        InputStream dateModeInputStream = new FileInputStream(getDateModel());
        TokenNameFinderModel dateTokenNameFinderModel = new TokenNameFinderModel(dateModeInputStream);
        NameFinderME dateNameFinderME = new NameFinderME(dateTokenNameFinderModel);
        finders.put("Dates", dateNameFinderME);
        InputStream locationModelInputStream = new FileInputStream(getLocationModel());
        TokenNameFinderModel locationTokenNameFinderModel = new TokenNameFinderModel(locationModelInputStream);
        NameFinderME locationNameFinderME = new NameFinderME(locationTokenNameFinderModel);
        finders.put("Locations", locationNameFinderME);
        tokenizer = SimpleTokenizer.INSTANCE;

    }

    public static File getPersonModel() {
        return new File(getModelDir(), OpenNLPModel.PersonNameFinder.getModelFileName());
    }

    public static File getModelDir() {
        String openNLPModelPath = System.getProperty("model.dir");
        return new File(openNLPModelPath);
    }

    public static File getDateModel() {
        return new File(getModelDir(), OpenNLPModel.DateNameFinder.getModelFileName());
    }

    public static File getLocationModel() {
        return new File(getModelDir(), OpenNLPModel.LocationNameFinder.getModelFileName());
    }
}

class Results {
    private List<Document> matches = new ArrayList<>();
    private Map<String, Sentence[]> sentences = new HashMap<>();

    public List<Document> getMatches() {
        return matches;
    }

    public void setMatches(List<Document> matches) {
        this.matches = matches;
    }

    public Map<String, Sentence[]> getSentences() {
        return sentences;
    }

    public void putSentences(String key, Sentence[] sentences) {
        this.sentences.put(key, sentences);
    }
}

class Sentence {
    private String sentence;
    private Map<String, List<String>> names = new HashMap<>();

    public Sentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentence() {
        return sentence;
    }

    public Map<String, List<String>> getNames() {
        return names;
    }

    public void putSentence(String key, List<String> values) {
        this.names.put(key, values);
    }
}