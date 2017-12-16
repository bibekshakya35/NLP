package scrape.bike;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BikeBrand {
    private String name;
    private String officialUrl;
    private String scrapeUrl;
    private String logo;
    private String fullName;
    private Set<ShowRoom> showRooms = new HashSet<>();
    private List<Bike> bikes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public Set<ShowRoom> getShowRooms() {
        return showRooms;
    }

    public void add(ShowRoom showRoom) {
        if (showRoom != null) {
            this.showRooms.add(showRoom);
        }
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void addBike(Bike bike) {
        if (bike != null) {
            this.bikes.add(bike);
        }
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getScrapeUrl() {
        return scrapeUrl;
    }

    public void setScrapeUrl(String scrapeUrl) {
        this.scrapeUrl = scrapeUrl;
    }

    public BikeBrand(String name) {
        this.name = name;
    }

    public BikeBrand() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "BikeBrand{" +
                "name='" + name + '\'' +
                ", officialUrl='" + officialUrl + '\'' +
                ", showRooms=" + showRooms +
                '}';
    }
}
