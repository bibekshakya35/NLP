package preprocess.constant;

/**
 * @author bibek on 12/22/17
 * @project compare&search
 */
public enum SpecCategory {
    Variants("Variants"),
    ChassisBrakesSuspensions("ChassisBrakesSuspensions"),
    Performance("Performance"),
    Transmission("Transmission"),
    EngineSpecifications("EngineSpecifications"),
    BodyExteriorDesignDimensions("BodyExteriorDesignDimensions");
    private final String annotationType;

    SpecCategory(String annotationType) {
        this.annotationType = annotationType;
    }

    public String getAnnotationType() {
        return annotationType;
    }
}
