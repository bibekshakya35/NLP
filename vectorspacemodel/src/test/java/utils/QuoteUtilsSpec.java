package utils;

import model.Quote;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author bibek on 1/13/18
 * @project vectorspacemodel
 */
public class QuoteUtilsSpec {
    @Test
    public void doQoutation(){
        List<Quote> quotes = QuoteUtils.produceList();
        Assert.assertNotNull(quotes);
        Assert.assertTrue(quotes.size()>0);
    }
}
