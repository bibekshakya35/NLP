package preprocess;


import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Fuel Mileage",
        "Acceleration",
        "Max Speed"
})
public class Performance {

    @JsonProperty("Fuel Mileage")
    private String fuelMileage;
    @JsonProperty("Acceleration")
    private String acceleration;
    @JsonProperty("Max Speed")
    private String maxSpeed;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Fuel Mileage")
    public String getFuelMileage() {
        return fuelMileage;
    }

    @JsonProperty("Fuel Mileage")
    public void setFuelMileage(String fuelMileage) {
        this.fuelMileage = fuelMileage;
    }

    @JsonProperty("Acceleration")
    public String getAcceleration() {
        return acceleration;
    }

    @JsonProperty("Acceleration")
    public void setAcceleration(String acceleration) {
        this.acceleration = acceleration;
    }

    @JsonProperty("Max Speed")
    public String getMaxSpeed() {
        return maxSpeed;
    }

    @JsonProperty("Max Speed")
    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
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