package preprocess.constant;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public enum  BodyExterior {
    Body_Type("Body Type"),
    Weight("Weight"),
    Length("Length"),
    Height("Height"),
    Width("Width"),
    Wheelbase("Wheelbase"),
    Ground_Clearance("Ground Clearance"),
    Fuel_Capacity("Fuel Capacity");
    private final String type;

    BodyExterior(String type) {
        this.type = type;
    }

    public static BodyExterior getType(String type) {
        for (BodyExterior c : BodyExterior.values()) {
            if (c.type.equalsIgnoreCase(type.trim())) {
                return c;
            }
        }
        return null;
    }
}
