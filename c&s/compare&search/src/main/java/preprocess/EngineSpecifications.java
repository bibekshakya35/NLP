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
        "Engine Type",
        "Torque",
        "Displacement",
        "Bore X Stroke",
        "Compression Ratio",
        "Carburettor",
        "Final Drive",
        "Starting System",
        "Idle Speed",
        "Power-to-Weight Ratio",
        "Power"
})
public class EngineSpecifications {

    @JsonProperty("Engine Type")
    private String engineType;
    @JsonProperty("Torque")
    private String torque;
    @JsonProperty("Displacement")
    private String displacement;
    @JsonProperty("Bore X Stroke")
    private String boreXStroke;
    @JsonProperty("Compression Ratio")
    private String compressionRatio;
    @JsonProperty("Carburettor")
    private String carburettor;
    @JsonProperty("Final Drive")
    private String finalDrive;
    @JsonProperty("Starting System")
    private String startingSystem;
    @JsonProperty("Idle Speed")
    private String idleSpeed;
    @JsonProperty("Power-to-Weight Ratio")
    private String powerToWeightRatio;
    @JsonProperty("Power")
    private String power;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Engine Type")
    public String getEngineType() {
        return engineType;
    }

    @JsonProperty("Engine Type")
    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    @JsonProperty("Torque")
    public String getTorque() {
        return torque;
    }

    @JsonProperty("Torque")
    public void setTorque(String torque) {
        this.torque = torque;
    }

    @JsonProperty("Displacement")
    public String getDisplacement() {
        return displacement;
    }

    @JsonProperty("Displacement")
    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    @JsonProperty("Bore X Stroke")
    public String getBoreXStroke() {
        return boreXStroke;
    }

    @JsonProperty("Bore X Stroke")
    public void setBoreXStroke(String boreXStroke) {
        this.boreXStroke = boreXStroke;
    }

    @JsonProperty("Compression Ratio")
    public String getCompressionRatio() {
        return compressionRatio;
    }

    @JsonProperty("Compression Ratio")
    public void setCompressionRatio(String compressionRatio) {
        this.compressionRatio = compressionRatio;
    }

    @JsonProperty("Carburettor")
    public String getCarburettor() {
        return carburettor;
    }

    @JsonProperty("Carburettor")
    public void setCarburettor(String carburettor) {
        this.carburettor = carburettor;
    }

    @JsonProperty("Final Drive")
    public String getFinalDrive() {
        return finalDrive;
    }

    @JsonProperty("Final Drive")
    public void setFinalDrive(String finalDrive) {
        this.finalDrive = finalDrive;
    }

    @JsonProperty("Starting System")
    public String getStartingSystem() {
        return startingSystem;
    }

    @JsonProperty("Starting System")
    public void setStartingSystem(String startingSystem) {
        this.startingSystem = startingSystem;
    }

    @JsonProperty("Idle Speed")
    public String getIdleSpeed() {
        return idleSpeed;
    }

    @JsonProperty("Idle Speed")
    public void setIdleSpeed(String idleSpeed) {
        this.idleSpeed = idleSpeed;
    }

    @JsonProperty("Power-to-Weight Ratio")
    public String getPowerToWeightRatio() {
        return powerToWeightRatio;
    }

    @JsonProperty("Power-to-Weight Ratio")
    public void setPowerToWeightRatio(String powerToWeightRatio) {
        this.powerToWeightRatio = powerToWeightRatio;
    }

    @JsonProperty("Power")
    public String getPower() {
        return power;
    }

    @JsonProperty("Power")
    public void setPower(String power) {
        this.power = power;
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