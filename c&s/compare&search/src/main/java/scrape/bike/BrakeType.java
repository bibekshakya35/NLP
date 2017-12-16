package scrape.bike;

/**
 * @author bibek on 12/5/17
 * @project compare&search
 */
public enum BrakeType {
    DRUM("Drum"),
    DISK("Disk");
    private final String brakeType;

    BrakeType(String brakeType) {
        this.brakeType = brakeType;
    }

    public String getBrakeType() {
        return brakeType;
    }
}
