package cspersistence.service;

import cspersistence.document.BikeProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * @author bibek on 12/31/17
 * @project cs-persistence
 */
public interface BikeProductService {
    List<String> getBikeBrand();

    Iterable<BikeProduct> saveAllBike(List<BikeProduct> bikeProducts);
    BikeProduct  saveBike(BikeProduct bikeProduct);
    void delete(BikeProduct bikeProduct);
    Optional<BikeProduct> findOne(String id);
    Iterable<BikeProduct> findAll();
    Page<BikeProduct> findByBrand(String brand, PageRequest pageRequest);
    List<BikeProduct> findByBikeName(String bikeName);
}
