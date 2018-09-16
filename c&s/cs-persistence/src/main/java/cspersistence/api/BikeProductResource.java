package cspersistence.api;

/**
 * @author bibek on 12/31/17
 * @project cs-persistence
 */

import cspersistence.service.BikeProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BikeProductResource {
    private BikeProductService bikeProductService;

    public BikeProductResource(BikeProductService bikeProductService) {
        this.bikeProductService = bikeProductService;
    }

    @GetMapping("bikes")
    public List<String> getBikes(){
        return this.bikeProductService.getBikeBrand();
    }
}
