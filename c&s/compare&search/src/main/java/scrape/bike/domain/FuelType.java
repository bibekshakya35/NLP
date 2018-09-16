package scrape.bike.domain;

/**
 * @author bibek on 12/5/17
 * @project compare&search
 */
public enum FuelType {
    PETROL("Petrol"),
    ELECTRICITY("Electricty");
    private final String type;

    FuelType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
