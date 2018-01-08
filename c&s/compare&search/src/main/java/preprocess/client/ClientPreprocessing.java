package preprocess.client;

import preprocess.BikeProduct;
import scrape.bike.BikeBrand;

import java.io.IOException;
import java.util.List;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public interface ClientPreprocessing {
    List<BikeProduct> normalized(List<BikeBrand> bikeBrandList);
    List<BikeBrand> clean(List<BikeBrand> bikeBrands)throws IOException;
}
