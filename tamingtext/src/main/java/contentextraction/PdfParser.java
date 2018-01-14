package contentextraction;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author bibek on 1/11/18
 * @project tamingtext
 */
public class PDFParser {
    public void parsePdf() throws FileNotFoundException{
        ContentHandler contentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream fileInputStream = new FileInputStream(new File("/home/bibek/bs-workspace/tamingtext_resources/files/pdfBox-sample.pdf"));
        ParseContext parseContext = new ParseContext();
        PDFParser pdfParser
    }
}
