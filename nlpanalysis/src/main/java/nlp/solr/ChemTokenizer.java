package nlp.solr;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChemTokenizer extends Tokenizer {
    protected CharTermAttribute charTermAttribute = addAttribute(CharTermAttribute.class);
    protected String stringToTokenize;
    protected int position = 0;
    protected List<int[]> chemicals = new ArrayList<>();

    @Override
    public final boolean incrementToken() throws IOException {
        //clear anything that is already saved in charTermAttribute
        this.charTermAttribute.setEmpty();
        //get the position of the next symbol
        int nextIndex = -1;
        Pattern p = Pattern.compile("[^A-zА-я]");
        Matcher m = p.matcher(stringToTokenize.substring(position));
        nextIndex = m.start();
        for (int[] pair : chemicals) {
            if (pair[0] < nextIndex && pair[1] > nextIndex) {
                if (position == pair[0]) {
                    nextIndex = pair[1];
                } else {
                    nextIndex = pair[0];
                }
            }
        }
        //next seperator was found
        if (nextIndex != -1) {
            String nextToken = stringToTokenize.substring(position, nextIndex);
            charTermAttribute.append(nextToken);
            position = nextIndex + 1;
            return true;
        }
        //last part of text
        else if (position < stringToTokenize.length()) {
            String nextToken = stringToTokenize.substring(position);
            charTermAttribute.append(nextToken);
            position = stringToTokenize.length();
            return true;
        } else {
            return false;
        }
    }

    public ChemTokenizer(Reader reader, List<String> additionalKeywords) {
        int numchars;
        char[] buffer = new char[1024];
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((numchars = reader.read(buffer, 0, buffer.length)) != -1) {
                stringBuilder.append(buffer, 0, numchars);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        stringToTokenize = stringBuilder.toString();
        //checking for keywords
        for (String keyword : additionalKeywords) {
            int[] tmp = new int[2];
            //start of keword
            tmp[0] = stringToTokenize.indexOf(keyword);
            tmp[1] = tmp[0] + keyword.length() - 1;
            chemicals.add(tmp);
        }
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        position = 0;
        chemicals = new ArrayList<>();
    }
}
