package preprocess;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author bibek on 12/20/17
 * @project compare&search
 */
public class BikeCleanerSpec {
    //@Test
    public void shouldCleanBrandName(){
        String name = "ACE BRITISH MOTORCYCLES";
        String result = BikeCleaner.cleanBikeBrandName(name);
        Assert.assertNotNull(result);
        Assert.assertEquals(result,"Ace British Motorcycles");
    }

}
