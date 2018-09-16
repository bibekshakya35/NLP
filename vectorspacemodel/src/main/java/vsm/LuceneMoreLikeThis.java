package vsm;

import model.Quote;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import utils.QuoteUtils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bibek on 1/13/18
 * @project vectorspacemodel
 */
public class LuceneMoreLikeThis {
    private static final Logger LOGGER = Logger.getLogger(LuceneMoreLikeThis.class.getName());

    public List<Quote> getSimilarQuotes() throws IOException{
        LOGGER.info("Creating RAMDirectory");
        RAMDirectory idx = new RAMDirectory();
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(
                Version.LUCENE_36,
                new StandardAnalyzer(Version.LUCENE_36));
        IndexWriter writer = new IndexWriter(idx,indexWriterConfig);
        List<Quote> quotes = QuoteUtils.produceList();
        //create a lucene document for each qoute add them to
        //RAMDirectory Index.  We include the db id so we can retrive the
        //similar quotes before returning them to the client.
        for (Quote quote :
             quotes) {
            Document document = new Document();
            document.add(new Field("contents",quote.getText(),Field.Store.YES,Field.Index.ANALYZED));
            document.add(new Field("id",String.valueOf(quote.getId()),Field.Store.YES,Field.Index.ANALYZED));
            writer.addDocument(document);
        }
        writer.commit();
        writer.close();
        //we are done writing documents to the index
        //Open the index
        IndexReader indexReader =IndexReader.open(idx);
        LOGGER.info("Term Frequency : "+indexReader.termDocs());
        LOGGER.log(Level.INFO,"Number of document is indexed has "+indexReader.numDocs());
        IndexSearcher is = new IndexSearcher(indexReader);
        MoreLikeThis moreLikeThis = new MoreLikeThis(indexReader);
        //lower some settings to MoreLikeThis will work with very short
//        //quotations

        moreLikeThis.setMinTermFreq(1);
        moreLikeThis.setMinDocFreq(1);
        //we need a reader to create the query so
        //using the string quoteText.
        Reader reader = new StringReader(quotes.get(2).getText());
        Query query =moreLikeThis.like(reader,"contents");
        //Search the index using the query and get the top 5 results
        TopDocs topDocs =is.search(query,3);
        LOGGER.info("FOUND : "+topDocs.totalHits +" topDocs");
        //Create an array to hold the quotes we are going to
        //pass back to the client
        List<Quote> foundQuotes = new ArrayList<>();
        for ( ScoreDoc scoreDoc : topDocs.scoreDocs ) {
            //This retrieves the actual Document from the index using
            //the document number. (scoreDoc.doc is an int that is the
            //doc's id
            Document doc = is.doc( scoreDoc.doc );

            //Get the id that we previously stored in the document from
            //hibernate and parse it back to a long.
            String idField =  doc.get("id");
            long id = Long.parseLong(idField);

            //retrieve the quote from Hibernate so we can pass
            //back an Array of actual Quote objects.
            Quote thisQuote = this.findQuote(quotes,id);

            //Add the quote to the array we'll pass back to the client
            foundQuotes.add(thisQuote);
        }

        return foundQuotes;
    }

    private Quote findQuote(List<Quote> quotes, long id) {
        Quote q= quotes.stream().filter(quote -> quote.getId()==id).findAny().orElse(null);
        return q;
    }

}