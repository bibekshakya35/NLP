package preprocess.constant;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public enum Transmission {
    TRANSMISSION("Transmission"),
    NUMBER_OF_GEAR("Number of gears"),
    CLUTCH("Clutch");
    private final String type;

    Transmission(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Transmission getType(String type) {
        for (Transmission c : Transmission.values()) {
            if (c.getType().equalsIgnoreCase(type.trim())) {
                return c;
            }
        }
        return null;
    }
}
