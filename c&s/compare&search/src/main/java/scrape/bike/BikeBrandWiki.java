package scrape.bike;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bibek on 12/2/17
 * @project compare&search
 */
public class BikeBrandWiki {
    private String brandName;
    private String wikiUrl;
    private Map<String, String> informations = new HashMap<>();

    public Map<String, String> getInformations() {
        return informations;
    }

    public void put(String key, String value) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
            this.informations.put(key, value);
        }
    }

    public BikeBrandWiki(String brandName, String wikiUrl) {
        this.brandName = brandName;
        this.wikiUrl = wikiUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }
}
