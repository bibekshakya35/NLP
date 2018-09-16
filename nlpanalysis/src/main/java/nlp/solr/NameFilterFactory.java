package nlp.solr;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;
import nlp.utils.NameFinderFactory;

import java.io.IOException;
import java.util.Map;


public class NameFilterFactory extends TokenFilterFactory {
    private NameFinderFactory factory;

    public NameFilterFactory(Map<String, String> args) {
        super(args);
        try {
            factory = new NameFinderFactory(args);
        } catch (IOException io) {
            throw (RuntimeException) new RuntimeException().initCause(io);
        }
    }


    @Override
    public TokenStream create(TokenStream input) {
        return new NameFilter(input, factory.getModelNames(), factory.getNameFinders());
    }
}
