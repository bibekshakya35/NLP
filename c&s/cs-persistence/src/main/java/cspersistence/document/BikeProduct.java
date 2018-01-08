package cspersistence.document;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import scrape.bike.domain.ShowRoom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(indexName = "bike_product",type = "bike_product")
public class BikeProduct {
    @Id
    private String id;
    private String name;
    private String price;
    private List<String> imageUrls;
    private String companyName;
    private String companyLogo;
    private String companyUrl;
    private BikeIdentity bikeIdentity;

    private Map<String,String> spec;
    private SpecWithCategory specWithCategory;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    private List<ShowRoom> showRooms;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }


    public void setPrice(String price) {
        this.price = price;
    }


    public List<String> getImageUrls() {
        return imageUrls;
    }


    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Map<String, String> getSpec() {
        return spec;
    }

    public void setSpec(Map<String, String> spec) {
        this.spec = spec;
    }

    public SpecWithCategory getSpecWithCategory() {
        return specWithCategory;
    }


    public void setSpecWithCategory(SpecWithCategory specWithCategory) {
        this.specWithCategory = specWithCategory;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }



    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public List<ShowRoom> getShowRooms() {
        return showRooms;
    }

    public void setShowRooms(List<ShowRoom> showRooms) {
        this.showRooms = showRooms;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public BikeIdentity getBikeIdentity() {
        return bikeIdentity;
    }

    public void setBikeIdentity(BikeIdentity bikeIdentity) {
        this.bikeIdentity = bikeIdentity;
    }
}
