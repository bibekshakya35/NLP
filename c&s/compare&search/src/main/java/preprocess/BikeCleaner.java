package preprocess;

import org.apache.commons.lang3.StringUtils;
import scrape.bike.BikeBrand;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * @author bibek on 12/20/17
 * @project compare&search
 */
public class BikeCleaner {
    private static Map<String,String> bikeBrandProperties =new HashMap<>();

    private static void propertiesSetting() {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            properties.load(classLoader.getResourceAsStream("brand_name.properties"));
            for (String key:
                 properties.stringPropertyNames()) {
                bikeBrandProperties.put(key,properties.get(key).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String cleanBikeName(final String bikeName) {
        String temporaryStr = bikeName;
        if (StringUtils.isNotBlank(temporaryStr)) {
            if (StringUtils.contains(temporaryStr, "RS.")) {
                String[] splitter = temporaryStr.split("RS.");
                return splitter[0].trim();
            }
            if (StringUtils.contains(temporaryStr, "– RS")) {
                String[] splitter = temporaryStr.split("– RS");
                return splitter[0].trim();
            }
            if (StringUtils.contains(temporaryStr, "-PRICE")) {
                String[] splitter = temporaryStr.split("-PRICE");
                return splitter[0].trim();
            }
            if (StringUtils.contains(temporaryStr, "– PRICE")) {
                String[] splitter = temporaryStr.split("– PRICE");
                return splitter[0].trim();
            }
            if (StringUtils.contains(temporaryStr, "- PRICE")) {
                String[] splitter = temporaryStr.split("- PRICE");
                return splitter[0].trim();

            }
            if (StringUtils.contains(temporaryStr, "PRICE")) {
                String[] splitter = temporaryStr.split("PRICE");
                return splitter[0].trim();
            }
            if (StringUtils.contains(temporaryStr, "This product is")) {
                String[] splitter = temporaryStr.split("This product is");
                return splitter[0].trim();
            }
        }
        return temporaryStr.trim();
    }

    public static String cleanBikeBrandName(final String name) {
        propertiesSetting();
        String bikeBrandInProperty = bikeBrandProperties.get(StringUtils.lowerCase(name));
        if(StringUtils.isNotBlank(bikeBrandInProperty)){
            return bikeBrandInProperty;
        }else{
            String temporaryStr = name;
            if (StringUtils.containsWhitespace(temporaryStr)) {

                StringBuilder nameBuilder = new StringBuilder();
                String[] spaceSlitter = temporaryStr.toLowerCase().split("\\s");
                if (spaceSlitter.length > 1) {
                    for (String temp : spaceSlitter) {
                        nameBuilder.append(temp.trim().substring(0, 1).toUpperCase())
                                .append(temp.trim().substring(1)).append(" ");

                    }
                }
                return nameBuilder.toString().trim();
            } else {
                return temporaryStr.toLowerCase().substring(0, 1).toUpperCase()
                        .concat(temporaryStr.substring(1));
            }
        }
    }

    public static String cleanBikePrice(final String price) {
        String temporayStr = price;
        if (StringUtils.isNotBlank(price)) {
            if (StringUtils.contains(price, "/")) {
                String[] splitter = price.split("/");
                return splitter[0].trim();
            }
            if (StringUtils.contains(price.toLowerCase(), "rs")) {
                String[] splitter = price.toLowerCase().split("rs");
                for (String split :
                        splitter) {
                    if (StringUtils.isNotBlank(split)) {
                        return split.trim();
                    }
                }
            }
        }
        return temporayStr;
    }

    public static List<String> cleanImageUrls(List<String> imageUrls) {
        List<String> picUrls = new ArrayList<>();
        imageUrls.forEach(pic -> {
            if (pic.endsWith("jpg")) {
                picUrls.add(pic);
            }
        });
        return picUrls;
    }
    public static void processCleansing(BikeBrand bikeBrand){
            String brandName = BikeCleaner.cleanBikeBrandName(bikeBrand.getName());
            bikeBrand.setName(brandName);
            bikeBrand.getBikes().forEach(bike -> {
                String bikeName = BikeCleaner.cleanBikeName(bike.getName());
                bike.setName(bikeName);
                String bikePrice = BikeCleaner.cleanBikePrice(bike.getPrice());
                bike.setPrice(bikePrice);
                List<String> bikeImageUrls = BikeCleaner.cleanImageUrls(bike.getImageUrls());
                bike.setImageUrls(bikeImageUrls);
            });
    }
}
