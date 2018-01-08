package preprocess.constant;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public enum ChasisBrakeSuspension {
    Chassis("Chassis"),
    FrontSuspension("Front Suspension"),
    RearSuspension("Rear Suspension"),
    FrontTyre("Front Tyre"),
    RearTyre("Rear Tyre"),
    SpokeType("Spoke Type"),
    FrontTyreBrake("Front Tyre Brake"),
    RearTyreBrake("Rear Tyre Brake"),
    SeatType("Seat Type");
    private final String type;

    ChasisBrakeSuspension(String type) {
        this.type = type;
    }

    public static ChasisBrakeSuspension getType(String type) {
        for (ChasisBrakeSuspension c : ChasisBrakeSuspension.values()) {
            if (c.type.equalsIgnoreCase(type.trim())) {
                return c;
            }
        }
        return null;
    }

}
