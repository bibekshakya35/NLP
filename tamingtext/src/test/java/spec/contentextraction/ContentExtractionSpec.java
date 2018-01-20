package spec.contentextraction;

import contentextraction.PdfParser;
import org.junit.Test;

/**
 * @author bibek on 1/11/18
 * @project tamingtext
 */
public class ContentExtractionSpec {
    @Test
    public void checkIfExtraction() throws Exception{
        PdfParser pdfParser = new PdfParser();
        pdfParser.parsePdf();
    }
}
