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
        "Transmission",
        "Number of gears",
        "Clutch"
})
public class Transmission {

    @JsonProperty("Transmission")
    private String transmission;
    @JsonProperty("Number of gears")
    private String numberOfGears;
    @JsonProperty("Clutch")
    private String clutch;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Transmission")
    public String getTransmission() {
        return transmission;
    }

    @JsonProperty("Transmission")
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    @JsonProperty("Number of gears")
    public String getNumberOfGears() {
        return numberOfGears;
    }

    @JsonProperty("Number of gears")
    public void setNumberOfGears(String numberOfGears) {
        this.numberOfGears = numberOfGears;
    }

    @JsonProperty("Clutch")
    public String getClutch() {
        return clutch;
    }

    @JsonProperty("Clutch")
    public void setClutch(String clutch) {
        this.clutch = clutch;
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