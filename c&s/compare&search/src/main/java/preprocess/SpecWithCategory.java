package preprocess;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Variants",
        "ChassisBrakesSuspensions",
        "Performance",
        "Transmission",
        "EngineSpecifications",
        "BodyExteriorDesignDimensions"
})
public class SpecWithCategory {

    @JsonProperty("Variants")
    private List<String> variants = null;
    @JsonProperty("ChasisBrakeSuspensions")
    private ChasisBrakeSuspensions chasisBrakeSuspensions;
    @JsonProperty("Performance")
    private Performance performance;
    @JsonProperty("Transmission")
    private Transmission transmission;
    @JsonProperty("EngineSpecifications")
    private EngineSpecifications engineSpecifications;
    @JsonProperty("BodyExteriorDesignDimensions")
    private BodyExteriorDesignDimensions bodyExteriorDesignDimensions;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Variants")
    public List<String> getVariants() {
        return variants;
    }

    @JsonProperty("Variants")
    public void setVariants(List<String> variants) {
        this.variants = variants;
    }

    @JsonProperty("ChasisBrakeSuspensions")
    public ChasisBrakeSuspensions getChasisBrakeSuspensions() {
        return chasisBrakeSuspensions;
    }

    @JsonProperty("ChasisBrakeSuspensions")
    public void setChasisBrakeSuspensions(ChasisBrakeSuspensions chasisBrakeSuspensions) {
        this.chasisBrakeSuspensions = chasisBrakeSuspensions;
    }

    @JsonProperty("Performance")
    public Performance getPerformance() {
        return performance;
    }

    @JsonProperty("Performance")
    public void setPerformance(Performance performance) {
        this.performance = performance;
    }

    @JsonProperty("Transmission")
    public Transmission getTransmission() {
        return transmission;
    }

    @JsonProperty("Transmission")
    public void setTransmission(Transmission transmission) {
        this.transmission = transmission;
    }

    @JsonProperty("EngineSpecifications")
    public EngineSpecifications getEngineSpecifications() {
        return engineSpecifications;
    }

    @JsonProperty("EngineSpecifications")
    public void setEngineSpecifications(EngineSpecifications engineSpecifications) {
        this.engineSpecifications = engineSpecifications;
    }

    @JsonProperty("BodyExteriorDesignDimensions")
    public BodyExteriorDesignDimensions getBodyExteriorDesignDimensions() {
        return bodyExteriorDesignDimensions;
    }

    @JsonProperty("BodyExteriorDesignDimensions")
    public void setBodyExteriorDesignDimensions(BodyExteriorDesignDimensions bodyExteriorDesignDimensions) {
        this.bodyExteriorDesignDimensions = bodyExteriorDesignDimensions;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}