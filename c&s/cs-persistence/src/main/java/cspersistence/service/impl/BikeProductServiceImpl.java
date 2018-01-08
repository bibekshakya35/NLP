package cspersistence.service.impl;

import cspersistence.document.BikeProduct;
import cspersistence.repository.BikeProductRepository;
import cspersistence.service.BikeProductService;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author bibek on 12/31/17
 * @project cs-persistence
 */
@Service
class BikeProductServiceImpl implements BikeProductService{
    private BikeProductRepository repository;

    public BikeProductServiceImpl(BikeProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<String> getBikeBrand() {
        Iterable<BikeProduct> bikeProductItr = this.repository
                .findAll();
        List<BikeProduct> bikeProducts = IteratorUtils.toList(bikeProductItr.iterator());
        List<String> companyNameFromIdentities = bikeProducts.
                stream().
                map(bikeProduct -> bikeProduct.
                getBikeIdentity().getCompanyName())
                .collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        List<String> companyNames = new ArrayList<>();
        companyNameFromIdentities.forEach(companyNameFromIdentity ->{
            companyNames.add(bikeProducts.stream().filter(
                    bikeProduct -> bikeProduct.getBikeIdentity().getCompanyName().equalsIgnoreCase(companyNameFromIdentity)
            ).findAny().get().getCompanyName());
        });
        return companyNames;
    }

    @Override
    public Iterable<BikeProduct> saveAllBike(List<BikeProduct> bikeProducts) {
        return this.repository.saveAll(bikeProducts);
    }

    @Override
    public BikeProduct saveBike(BikeProduct bikeProduct) {
        BikeProduct bikeProductAfter =  this.repository.save(bikeProduct);
        return bikeProductAfter;
    }

    @Override
    public void delete(BikeProduct bikeProduct) {
        this.repository.delete(bikeProduct);
    }

    @Override
    public Optional<BikeProduct> findOne(String id) {
        return null;
    }

    @Override
    public Iterable<BikeProduct> findAll() {
        return null;
    }

    @Override
    public Page<BikeProduct> findByBrand(String brand, PageRequest pageRequest) {
        return null;
    }

    @Override
    public List<BikeProduct> findByBikeName(String bikeName) {
        return null;
    }
}
