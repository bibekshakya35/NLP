package scrape.bike.repository;

import scrape.bike.BikeBrand;

import java.util.List;

/**
 * @author bibek on 12/1/17
 * @project compare&search
 */
public interface BikeBrandRepository {
    void add(BikeBrand bikeBrand);
    List<BikeBrand> getBikeBrands();
}
