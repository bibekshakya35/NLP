package wordanalysis;

import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.AbstractExternalizable;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Version;

import java.io.*;

public class LuceneAnalyzerTokenizerFactory implements TokenizerFactory, Serializable {
    private Analyzer analyzer;
    private String field;

    public LuceneAnalyzerTokenizerFactory(Analyzer analyzer, String field) {
        this.analyzer = analyzer;
        this.field = field;
    }

    private Object writeReplace() {
        return new Serializer(this);
    }

    @Override
    public Tokenizer tokenizer(char[] ch, int start, int length) {
        Reader reader = new CharArrayReader(ch, start, length);
        TokenStream tokenStream = null;
        try {
            tokenStream = analyzer.tokenStream(field, reader);
            tokenStream.reset();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return new LuceneTokenStreamTokenizer(tokenStream);
    }

    private static class Serializer extends AbstractExternalizable {
        private LuceneAnalyzerTokenizerFactory latf;

        public Serializer(LuceneAnalyzerTokenizerFactory latf) {
            this.latf = latf;
        }

        @Override
        protected Object read(ObjectInput in) throws ClassNotFoundException, IOException {
            Analyzer analyzer = (Analyzer) in.readObject();
            String field = in.readUTF();
            return new LuceneAnalyzerTokenizerFactory(analyzer, field);
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(latf.analyzer);
            out.writeUTF(latf.field);
        }
    }

    static class LuceneTokenStreamTokenizer extends Tokenizer {
        private TokenStream tokenStream;
        private CharTermAttribute termAttribute;
        private OffsetAttribute offsetAttribute;

        private int lastTokenStartPosition = -1;
        private int lastTokenEndPosition = -1;

        public LuceneTokenStreamTokenizer(TokenStream tokenStream) {
            this.tokenStream = tokenStream;
            termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
        }

        @Override
        public String nextToken() {
            try {
                if (tokenStream.incrementToken()) {
                    lastTokenStartPosition = offsetAttribute.startOffset();
                    lastTokenEndPosition = offsetAttribute.endOffset();
                    return termAttribute.toString();
                } else {
                    endAndClose();
                    return null;
                }
            } catch (IOException e) {
                endAndClose();
                return null;
            }
        }

        @Override
        public String nextWhitespace() {
            return "default";
        }

        @Override
        public int lastTokenStartPosition() {
            return lastTokenStartPosition;
        }

        @Override
        public int lastTokenEndPosition() {
            return lastTokenEndPosition;
        }

        private void endAndClose() {

            try {
                tokenStream.end();
            } catch (IOException e) {
                //Cant do anything with this.
            }
            try {
                tokenStream.close();
            } catch (IOException e) {
                //Cant do anything with this.
            }
        }
    }

    public static void main(String[] args) {
        String text = "Hi how are you? Are the numbers 1 2 3 4.5 all integers?";
        Analyzer analyzer =
                new StandardAnalyzer(Version.LUCENE_40);
        TokenizerFactory tokenizerFactory = new LuceneAnalyzerTokenizerFactory(analyzer, "DEFAULT");
        Tokenizer tokenizer = tokenizerFactory.tokenizer(text.toCharArray(), 0, text.length());
        String token = null;
        while ((token = tokenizer.nextToken()) != null) {
            String ws = tokenizer.nextWhitespace();
            System.out.println("Token:'" + token + "'");
            System.out.println("WhiteSpace:'" + ws + "'");
        }
    }
}
