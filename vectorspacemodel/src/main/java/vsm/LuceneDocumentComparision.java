package vsm;

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVectorFormat;
import org.apache.commons.math3.linear.SparseRealVector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.*;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bibek on 1/7/18
 * @project vectorspacemodel
 */
public class LuceneDocumentComparision {
    public static final String FILES_TO_INDEX_DIRECTORY = "/home/bibek/bs-workspace/filesToIndex";
    public static final String FIELD_PATH = "path";
    public static final String FIELD_CONTENTS = "contents";
    private static Directory ramDirectory = new RAMDirectory();

    public static void main(String[] args) throws Exception {
        createIndex();
        searchIndex("Bibek");
        testSimilarityUsingCosine();
    }

    private static void testSimilarityUsingCosine() throws Exception{
        IndexReader reader = IndexReader.open(ramDirectory);
        //first find all the terms in the index
        Map<String,Integer> terms = new HashMap<>();
        TermEnum termEnum = reader.terms(new Term("contents"));
        int pos = 0;
        while (termEnum.next()){
            Term term = termEnum.term();
            if(!"contents".equalsIgnoreCase(term.field())){
                break;
            }
            terms.put(term.text(),pos++);
        }
        int [] docIds = new int[]{0,1};
        DocVector[] docVectors = new DocVector[docIds.length];
        int i=0;
        for (int docId:docIds){
            TermFreqVector[] tfvs = reader.getTermFreqVectors(docId);
            docVectors[i] = new DocVector(terms);
            for (TermFreqVector tfv : tfvs){
                String[] termTexts = tfv.getTerms();
                int[] termFreq = tfv.getTermFrequencies();
                for (int j=0;j<termTexts.length;j++){
                    docVectors[i].setEntry(termTexts[j],termFreq[j]);
                }
            }
            docVectors[i].normalized();
            i++;
        }
        System.out.println(i);
        //now get Similarities between docVector[0] and docVector[1]
        double cosine = getCosineSimilarity(docVectors[0],docVectors[1]);
        System.out.println("Cosine similarity between documen "+cosine);
        reader.close();
    }

    private static void searchIndex(String searchStr) throws IOException, ParseException {
        System.out.println("Searching for " + searchStr);
        IndexReader indexReader = IndexReader.open(ramDirectory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        QueryParser queryParser = new QueryParser(Version.LUCENE_36, FIELD_CONTENTS, analyzer);
        Query query = queryParser.parse(searchStr);
        TopDocs topDocs = indexSearcher.search(query, 100);
        System.out.println("Number of hits : " + topDocs.totalHits);
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            System.out.println("Doc ID " + docId);
            Document document = indexSearcher.doc(docId);
            TermFreqVector termFreqVector = indexReader.getTermFreqVector(docId, FIELD_CONTENTS);
            System.out.println("Term Frequency vector : " + termFreqVector);
        }
        System.out.println("Found " + hits.length);
    }

    private static void createIndex() throws IOException {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
        IndexWriter indexWriter = new IndexWriter(ramDirectory, new IndexWriterConfig(Version.LUCENE_36, analyzer));
        File dir = new File(FILES_TO_INDEX_DIRECTORY);
        File[] files = dir.listFiles();
        for (File file : files) {
            Document document = new Document();
            String path = file.getCanonicalPath();
            document.add(new Field(FIELD_PATH, path, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
            Reader reader = new FileReader(file);
            document.add(new Field(FIELD_CONTENTS, reader, Field.TermVector.YES));
            indexWriter.addDocument(document);
        }
        indexWriter.commit();
        indexWriter.close();
    }
    private static class DocVector{
        public Map<String,Integer> terms;
        public SparseRealVector vector;

        public DocVector(Map<String, Integer> terms) {
            this.terms = terms;
            this.vector = new OpenMapRealVector(terms.size());
        }
        public void setEntry(String term,int freq){
            if (terms.containsKey(term)){
                int pos = terms.get(term);
                vector.setEntry(pos,(double)freq);
            }
        }
        public void normalized(){
            double sum = vector.getL1Norm();
            vector =(SparseRealVector)vector.mapDivide(sum);
        }

        @Override
        public String toString() {
            RealVectorFormat format = new RealVectorFormat();
            return format.format(vector);
        }
    }
    private static double getCosineSimilarity(DocVector docVector1,DocVector docVector2){
        return
                (docVector1.vector.dotProduct(docVector2.vector))/
                        (docVector1.vector.getNorm()*docVector2.vector.getNorm());
    }
    
}
