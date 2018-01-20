package contentextraction;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * @author bibek on 1/11/18
 * @project tamingtext
 */
public class ContentExtraction {
    public void provideAutoDetectParser() throws FileNotFoundException,IOException,SAXException,TikaException{
        InputStream inputStream = new FileInputStream(new File("/home/bibek/bs-workspace/tamingtext_resources/files/pdfBox-sample.pdf"));
        ContentHandler
                contentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        Parser parser = new AutoDetectParser();

        ParseContext parseContext = new ParseContext();

        parser.parse(inputStream,contentHandler,metadata,parseContext);
        System.out.println("Title : "+metadata.get(Metadata.TITLE));
        System.out.println("Body : "+contentHandler.toString());

    }
}
