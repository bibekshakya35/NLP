package spec.parser;

import org.junit.Test;
import nlp.sentence.parse.SentenceParse;

import java.io.IOException;

/**
 * @author bibek on 1/10/18
 * @project tamingtext
 */
public class SentenceParserSpec {
    @Test
    public void doParseOPENNLP() throws IOException {
        SentenceParse sentenceParse = new SentenceParse();
        sentenceParse.parseOPENNLP();
    }
}
