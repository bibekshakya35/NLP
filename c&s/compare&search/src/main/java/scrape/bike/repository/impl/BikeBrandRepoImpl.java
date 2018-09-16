package scrape.bike.repository.impl;

import scrape.bike.BikeBrand;
import scrape.bike.repository.BikeBrandRepository;

import java.util.ArrayList;
import java.util.List;

public class BikeBrandRepoImpl implements BikeBrandRepository {
    private List<BikeBrand> bikeBrandList = new ArrayList<>();

    @Override
    public List<BikeBrand> getBikeBrands() {
        return bikeBrandList;
    }

    @Override
    public void add(BikeBrand bikeBrand) {
        bikeBrandList.add(bikeBrand);
    }
}
