package nlp.chapterone;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;

/**
 * @author bibek on 12/16/17
 * @project tamingtext
 */
public class HelloLucene {
    public static void main(String[] args) throws IOException, ParseException {
        //0. specify the analzer for tokenizing text
        // the same analyzer should be used for indexing and searching
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //1. Create the index
        Directory indexDir = new RAMDirectory();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(indexDir, indexWriterConfig);
        addDoc(indexWriter, "Lucene in Action", "152222222");
        addDoc(indexWriter, "Lucene for Dummies", "55320055Z");
        addDoc(indexWriter, "Managing Gigabytes", "55063554A");
        addDoc(indexWriter, "The Art of Computer Science", "9900333X");
        indexWriter.close();

        //2. QUery
        String queryStr = args.length > 0 ? args[0] : "Lucene";
        //the title args specifies the default field to use
        //when no field is explicitly specified in the query
        Query query = new QueryParser("title", analyzer).parse(queryStr);
        //3. search
        int hitsPerPage = 10;
        IndexReader reader = DirectoryReader.open(indexDir);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(query, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;
        //4 Display the result
        System.out.println("Found "+hits.length +" hits");
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document document = searcher.doc(docId);
            System.out.println((i + 1)+".  "+document.get("isbn")+"\t"+document.get("title"));
        }
        reader.close();

    }

    private static void addDoc(IndexWriter indexWriter, String bookName, String ibns) throws IOException {
        Document document = new Document();
        document.add(new TextField("title", bookName, Field.Store.YES));
        //USE A STRING FIELD FOR ISBN BECAUSE WE DONT WANT IT TOKENIZED
        document.add(new StringField("isbn",ibns,Field.Store.YES));
        indexWriter.addDocument(document);
    }
}
