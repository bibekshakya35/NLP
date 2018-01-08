package preprocess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import scrape.bike.domain.ShowRoom;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "price",
        "imageUrls",
        "spec",
        "specWithCategory"
})
public class BikeProduct {
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private String price;
    @JsonProperty("imageUrls")
    private List<String> imageUrls = null;
    private String companyName;
    private String companyLogo;
    private String companyUrl;
    private BikeIdentity bikeIdentity;

    @JsonProperty("spec")
    private Map<String,String> spec;
    @JsonProperty("specWithCategory")
    private SpecWithCategory specWithCategory;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    @JsonProperty("showrooms")
    private List<ShowRoom> showRooms;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("imageUrls")
    public List<String> getImageUrls() {
        return imageUrls;
    }

    @JsonProperty("imageUrls")
    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public Map<String, String> getSpec() {
        return spec;
    }

    public void setSpec(Map<String, String> spec) {
        this.spec = spec;
    }
    @JsonProperty("specWithCategory")
    public SpecWithCategory getSpecWithCategory() {
        return specWithCategory;
    }

    @JsonProperty("specWithCategory")
    public void setSpecWithCategory(SpecWithCategory specWithCategory) {
        this.specWithCategory = specWithCategory;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
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
