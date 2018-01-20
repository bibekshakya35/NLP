package contentextraction;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author bibek on 1/11/18
 * @project tamingtext
 */
public class PdfParser {
    public void parsePdf() throws IOException, TikaException, SAXException {
        ContentHandler contentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream fileInputStream = new FileInputStream(new File("/home/bibek/bs-workspace/tamingtext_resources/files/pdfBox-sample.pdf"));
        ParseContext parseContext = new ParseContext();
        PDFParser pdfParser = new PDFParser();
        pdfParser.parse(fileInputStream,contentHandler,metadata,parseContext);
        //getting the content of the document
        System.out.println("Contents of the PDF : "+contentHandler.toString());
        //getting meta data of the document
        System.out.println("Metadata of the PDF");
        System.out.println("Title :"+metadata.get(Metadata.TITLE));
        String[] metadataNames = metadata.names();
        for (String name : metadataNames){
            System.out.println(name);
        }
    }
}
