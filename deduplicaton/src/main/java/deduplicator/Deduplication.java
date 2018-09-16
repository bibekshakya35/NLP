package deduplicator;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Deduplication {
    private final Directory directory;
    private final float scoreThreshold;
    Deduplication(List<String> results, float scoreThreshold){
        //use memory to hold lucene index
        this.directory = new RAMDirectory();
        this.scoreThreshold =scoreThreshold;
        //Add All contents to lucene index
        addContents(results);
    }
    public List<String> dedup(){
        List<String> results = new ArrayList<>();
        IndexReader indexReader = null;
        try {
            indexReader = IndexReader.open(directory,false);

            int maxDocumentNumber = indexReader.maxDoc();
            for (int docNum = 0; docNum < maxDocumentNumber; docNum++) {
                if (indexReader.isDeleted(docNum)) continue;
                //save document to result list
                Document doc = indexReader.document(docNum);
                String content = getContentFromDoc(doc);
                results.add(content);
                Reader reader = new StringReader(content);
                MoreLikeThis moreLikeThis = new MoreLikeThis(indexReader);
                //lower the frequency since content is short
                moreLikeThis.setMinTermFreq(1);
                moreLikeThis.setMinDocFreq(1);
                Query query = moreLikeThis.like( reader,"contents");
                //Find Similar document

                IndexSearcher indexSearcher = new IndexSearcher(indexReader);

                TopDocs topDocs =indexSearcher.search(query,100);
                for (ScoreDoc scoreDoc : topDocs.scoreDocs){
                    if (scoreDoc.score>scoreThreshold){
                        //delete all the similar document
                        indexReader.deleteDocument(scoreDoc.doc);
                    }
                }
                closeSearcher(indexSearcher);
            }
            closeIndexReader(indexReader);
        } catch (IOException e) {
            e.printStackTrace();
            closeIndexReader(indexReader);
        }
        return results;
    }

    private void closeIndexReader(IndexReader indexReader) {
        if (indexReader != null) {
            try {
                indexReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeSearcher(IndexSearcher indexSearcher) {
        if (indexSearcher != null) {
            try {
                indexSearcher.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getContentFromDoc(Document doc) {
        return doc.get("contents");
    }
    private void addContents(List<String> results) {
        IndexWriter writer =null;
        try{
            writer = getIndexWriter();
            for (String content:
                    results) {
                writer.addDocument(addContentToDoc(content));
            }
            closeIndexWriter(writer);   
        }catch (IOException e){
            e.printStackTrace();
            closeIndexWriter(writer);
        }
    }

    private void closeIndexWriter(IndexWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (CorruptIndexException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private Document addContentToDoc(String content) {
        Document doc = new Document();
        doc.add(new Field("contents", content, Field.Store.YES,
                Field.Index.ANALYZED));
        return doc;
    }

    public IndexWriter getIndexWriter()throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
                Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35));
        return new IndexWriter(directory, indexWriterConfig);
    }
}
