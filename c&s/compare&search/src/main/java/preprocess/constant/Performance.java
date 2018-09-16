package preprocess.constant;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public enum Performance {
    FUEL_MILEAGE("Fuel Mileage"),
    ACCELERATION("Acceleration"),
    MAX_SPEED("Max Speed");
    private final String type;

    Performance(String type) {
        this.type = type;
    }

    public static Performance getType(String type) {
        for (Performance c : Performance.values()) {
            if (c.type.equalsIgnoreCase(type.trim())) {
                return c;
            }
        }
        return null;
    }
}
