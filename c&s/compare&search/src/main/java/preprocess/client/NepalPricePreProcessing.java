package preprocess.client;

import preprocess.BikeCleaner;
import preprocess.BikeIdentity;
import preprocess.BikeProduct;
import com.google.gson.reflect.TypeToken;
import scrape.bike.BikeBrand;
import util.JsonUtils;
import util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
class NepalPricePreProcessing implements ClientPreprocessing {
    public List<BikeBrand> clean(List<BikeBrand> newPriceList) throws IOException {
        Type bikeBrandClazz = new TypeToken<ArrayList<BikeBrand>>() {
        }.getType();
        String bikeBrand = org.apache.commons.io.FileUtils.readFileToString(new File("/home/bibek/bs-workspace/bike/preprocess/scrape/newpricenepal.json"), StandardCharsets.UTF_8);
        newPriceList = JsonUtils.fromJsonToList(bikeBrand, bikeBrandClazz);
        if (!newPriceList.isEmpty()) {
            newPriceList.forEach(newPrice -> {
                BikeCleaner.processCleansing(newPrice);
            });
        }
        org.apache.commons.io.FileUtils.writeStringToFile(new File("/home/bibek/bs-workspace/bike/preprocess/clean/newpricenepal.json"), JsonUtils.toJsonList(newPriceList), StandardCharsets.UTF_8);
        return newPriceList;
    }

    @Override
    public List<BikeProduct> normalized(List<BikeBrand> bikeBrandList) {
        List<BikeProduct> bikeProducts = new ArrayList<>();
        bikeBrandList.forEach(b -> {
            BikeProduct bikeProduct = new BikeProduct();
            bikeProduct.setCompanyLogo(b.getLogo());
            bikeProduct.setCompanyUrl(b.getOfficialUrl());
            bikeProduct.setCompanyName(b.getName());
            bikeProduct.setShowRooms(new ArrayList<>(b.getShowRooms()));
            b.getBikes().forEach(bike -> {
                bikeProduct.setName(bike.getName());
                bikeProduct.setPrice(bike.getPrice());
                bikeProduct.setImageUrls(bike.getImageUrls());
                bikeProduct.setBikeIdentity(new BikeIdentity(b.getName(),bike.getName()));
                Map<String,String> specs = new HashMap<>();
                bike.getSpec().forEach(spec->{
                    String[] splitter = spec.split(":");
                    List<String> keyValueList = new ArrayList<>();
                    for(String kv:splitter ){
                        if(StringUtils.isNotBlank(kv)){
                            keyValueList.add(kv);
                        }
                    }
                    if(keyValueList.size()==2){
                        specs.put(keyValueList.get(0),keyValueList.get(1));
                        bikeProduct.setSpec(specs);
                    }

                });
                bikeProducts.add(bikeProduct);
            });
        });
        return bikeProducts;
    }
}
