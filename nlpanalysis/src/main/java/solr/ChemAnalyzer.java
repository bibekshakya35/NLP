package solr;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

import java.util.List;

public class ChemAnalyzer extends Analyzer {
    List<String> additionKeywords;

    public void setAdditionKeywords(List<String> additionKeywords) {
        this.additionKeywords = additionKeywords;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(new WhitespaceTokenizer());
    }
}
