package wordanalysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import java.io.*;
import java.util.Collections;

public class RunLuceneTokenizer {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("type a sentence below to see the tokens and white spaces:");
            String input = reader.readLine();
            Reader stringReader = new StringReader(input);
            TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_40, stringReader);
            tokenStream = new LowerCaseFilter(Version.LUCENE_40, tokenStream);
            CharTermAttribute terms = tokenStream.addAttribute(CharTermAttribute.class);
            OffsetAttribute offset = tokenStream.addAttribute(OffsetAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                String token = terms.toString();
                int start = offset.startOffset();
                int end = offset.endOffset();
                System.out.println("Token:'" + token + "'" + " Start: " + start + " End:" + end);
            }
            tokenStream.end();
            tokenStream.close();
        }
    }
}
