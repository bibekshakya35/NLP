package scrape.google;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bibek on 11/27/17
 * @project compare&search
 */
public class GoogleCustomSearchApiSpec {
    @Test
    public void shouldProvideQuery(){
        GoogleCustomSearchApi g = new GoogleCustomSearchApi();
        Assert.assertNotNull(g.getUri("Hero Honda Official Site"));
        System.out.print(g.getUri("Hero Honda Official Site"));
    }
}
