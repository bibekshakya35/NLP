package scrape.bike;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bibek on 12/2/17
 * @project compare&search
 */
public class WikiBike {
    private String countryName;
    private List<BikeBrandWiki> bikeBrandWikis = new ArrayList<>();


    public WikiBike(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public List<BikeBrandWiki> getBikeBrandWikis() {
        return bikeBrandWikis;
    }

    public void add(BikeBrandWiki bikeBrandWiki) {
        this.bikeBrandWikis.add(bikeBrandWiki);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WikiBike wikiBike = (WikiBike) o;

        return countryName.equals(wikiBike.countryName);
    }

    @Override
    public int hashCode() {
        return countryName.hashCode();
    }
}
