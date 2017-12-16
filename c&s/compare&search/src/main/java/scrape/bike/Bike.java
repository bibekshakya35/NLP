package scrape.bike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bike {
    private String name;
    private String price;
    private List<String> imageUrls = new ArrayList<>();
    private List<String> spec = new ArrayList<>();
    private Map<String, List<String>> specWithCategory = new HashMap<>();
    private Specification specification;




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

    public List<String> getSpec() {
        return spec;
    }

    public void addSpec(String spec) {
        this.spec.add(spec);
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void add(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }

    public Map<String, List<String>> getSpecWithCategory() {
        return specWithCategory;
    }

    public void addSpecWithCategory(String key, List<String> values) {
        this.specWithCategory.put(key, values);
    }
}
