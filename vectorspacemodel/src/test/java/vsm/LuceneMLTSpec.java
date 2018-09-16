package vsm;

import model.Quote;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author bibek on 1/13/18
 * @project vectorspacemodel
 */
public class LuceneMLTSpec {
    @Test
    public void test() throws IOException{
        List<Quote> quotes = new LuceneMoreLikeThis().getSimilarQuotes();
        Assert.assertNotNull(quotes);
    }
}
