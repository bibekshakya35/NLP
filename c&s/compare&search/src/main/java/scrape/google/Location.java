package scrape.google;

public enum Location {
    KATHMANDU("27.7172,85.3240",5000);
    private final String location;
    private final long radius;
    Location(String location,long radius) {
        this.location = location;
        this.radius = radius;
    }

    public String getLocation() {
        return location;
    }

    public long getRadius() {
        return radius;
    }
}
