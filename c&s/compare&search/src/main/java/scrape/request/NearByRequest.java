package scrape.request;

import scrape.google.Location;

public class NearByRequest {
    private String location;
    private long radius;
    private String keyword;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getRadius() {
        return radius;
    }

    public void setRadius(long radius) {
        this.radius = radius;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    private NearByRequest(String location, long radius, String keyword) {
        this.location = location;
        this.radius = radius;
        this.keyword = keyword;
    }

    public static NearByRequest forKathmandu(String keyword){
        return new NearByRequest(Location.KATHMANDU.getLocation(), Location.KATHMANDU.getRadius(),keyword);
    }
}
