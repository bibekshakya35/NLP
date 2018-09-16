package preprocess;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author bibek on 12/20/17
 * @project compare&search
 */
public class BikePreprocessingImplSpec {
    //@Test
    public void shouldGiveListOfBikeName() {

        BikePreProcessing bikePreProcessing = new BikePreprocessingImpl();
        bikePreProcessing.cleaning();
        List<String> nameOfBikes = bikePreProcessing.nameOfBike();
        Assert.assertTrue(nameOfBikes.size() > 1);

    }
    //@Test
    public void shouldGiveYouBikeNormalized(){
        BikePreProcessing bikePreProcessing = new BikePreprocessingImpl();
        bikePreProcessing.cleaning();
        List<BikeProduct> bikeProducts = bikePreProcessing.normalized();
        //bikeProducts = bikePreProcessing.transformation();
        Assert.assertTrue(bikeProducts.size()>0);
    }

}
