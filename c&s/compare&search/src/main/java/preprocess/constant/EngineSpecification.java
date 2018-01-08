package preprocess.constant;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public enum EngineSpecification {
    ENGINE_TYPE("Engine Type"),
    TORQUE("Torque"),
    DISPLACEMENT("Displacement"),
    BORE_X_STROKE("Bore X Stroke"),
    Compression_Ratio("Compression Ratio"),
    Carburettor("Carburettor"),
    FINAL_DRIVE("Final Drive"),
    Starting_System("Starting System"),
    Idle_Speed("Idle Speed"),
    Power_to_Weight_Ratio("Power-to-Weight Ratio"),
    POWER("Power");
    private final String type;

    EngineSpecification(String type) {
        this.type = type;
    }

    public static EngineSpecification getType(String type) {
        for (EngineSpecification c : EngineSpecification.values()) {
            if (c.type.equalsIgnoreCase(type.trim())) {
                return c;
            }
        }
        return null;
    }
}
