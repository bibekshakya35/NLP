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
        "Chassis",
        "Front Suspension",
        "Rear Suspension",
        "Front Tyre",
        "Rear Tyre",
        "Spoke Type",
        "Front Tyre Brake",
        "Rear Tyre Brake",
        "Seat Type"
})
public class ChasisBrakeSuspensions {

    @JsonProperty("Chassis")
    private String chassis;
    @JsonProperty("Front Suspension")
    private String frontSuspension;
    @JsonProperty("Rear Suspension")
    private String rearSuspension;
    @JsonProperty("Front Tyre")
    private String frontTyre;
    @JsonProperty("Rear Tyre")
    private String rearTyre;
    @JsonProperty("Spoke Type")
    private String spokeType;
    @JsonProperty("Front Tyre Brake")
    private String frontTyreBrake;
    @JsonProperty("Rear Tyre Brake")
    private String rearTyreBrake;
    @JsonProperty("Seat Type")
    private String seatType;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Chassis")
    public String getChassis() {
        return chassis;
    }

    @JsonProperty("Chassis")
    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    @JsonProperty("Front Suspension")
    public String getFrontSuspension() {
        return frontSuspension;
    }

    @JsonProperty("Front Suspension")
    public void setFrontSuspension(String frontSuspension) {
        this.frontSuspension = frontSuspension;
    }

    @JsonProperty("Rear Suspension")
    public String getRearSuspension() {
        return rearSuspension;
    }

    @JsonProperty("Rear Suspension")
    public void setRearSuspension(String rearSuspension) {
        this.rearSuspension = rearSuspension;
    }

    @JsonProperty("Front Tyre")
    public String getFrontTyre() {
        return frontTyre;
    }

    @JsonProperty("Front Tyre")
    public void setFrontTyre(String frontTyre) {
        this.frontTyre = frontTyre;
    }

    @JsonProperty("Rear Tyre")
    public String getRearTyre() {
        return rearTyre;
    }

    @JsonProperty("Rear Tyre")
    public void setRearTyre(String rearTyre) {
        this.rearTyre = rearTyre;
    }

    @JsonProperty("Spoke Type")
    public String getSpokeType() {
        return spokeType;
    }

    @JsonProperty("Spoke Type")
    public void setSpokeType(String spokeType) {
        this.spokeType = spokeType;
    }

    @JsonProperty("Front Tyre Brake")
    public String getFrontTyreBrake() {
        return frontTyreBrake;
    }

    @JsonProperty("Front Tyre Brake")
    public void setFrontTyreBrake(String frontTyreBrake) {
        this.frontTyreBrake = frontTyreBrake;
    }

    @JsonProperty("Rear Tyre Brake")
    public String getRearTyreBrake() {
        return rearTyreBrake;
    }

    @JsonProperty("Rear Tyre Brake")
    public void setRearTyreBrake(String rearTyreBrake) {
        this.rearTyreBrake = rearTyreBrake;
    }

    @JsonProperty("Seat Type")
    public String getSeatType() {
        return seatType;
    }

    @JsonProperty("Seat Type")
    public void setSeatType(String seatType) {
        this.seatType = seatType;
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