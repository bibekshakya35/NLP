package preprocess;

/**
 * @author bibek on 12/20/17
 * @project compare&search
 */
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
        "Body Type",
        "Weight",
        "Length",
        "Height",
        "Width",
        "Wheelbase",
        "Ground Clearance",
        "Fuel Capacity"
})
public class BodyExteriorDesignDimensions {

    @JsonProperty("Body Type")
    private String bodyType;
    @JsonProperty("Weight")
    private String weight;
    @JsonProperty("Length")
    private String length;
    @JsonProperty("Height")
    private String height;
    @JsonProperty("Width")
    private String width;
    @JsonProperty("Wheelbase")
    private String wheelbase;
    @JsonProperty("Ground Clearance")
    private String groundClearance;
    @JsonProperty("Fuel Capacity")
    private String fuelCapacity;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Body Type")
    public String getBodyType() {
        return bodyType;
    }

    @JsonProperty("Body Type")
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    @JsonProperty("Weight")
    public String getWeight() {
        return weight;
    }

    @JsonProperty("Weight")
    public void setWeight(String weight) {
        this.weight = weight;
    }

    @JsonProperty("Length")
    public String getLength() {
        return length;
    }

    @JsonProperty("Length")
    public void setLength(String length) {
        this.length = length;
    }

    @JsonProperty("Height")
    public String getHeight() {
        return height;
    }

    @JsonProperty("Height")
    public void setHeight(String height) {
        this.height = height;
    }

    @JsonProperty("Width")
    public String getWidth() {
        return width;
    }

    @JsonProperty("Width")
    public void setWidth(String width) {
        this.width = width;
    }

    @JsonProperty("Wheelbase")
    public String getWheelbase() {
        return wheelbase;
    }

    @JsonProperty("Wheelbase")
    public void setWheelbase(String wheelbase) {
        this.wheelbase = wheelbase;
    }

    @JsonProperty("Ground Clearance")
    public String getGroundClearance() {
        return groundClearance;
    }

    @JsonProperty("Ground Clearance")
    public void setGroundClearance(String groundClearance) {
        this.groundClearance = groundClearance;
    }

    @JsonProperty("Fuel Capacity")
    public String getFuelCapacity() {
        return fuelCapacity;
    }

    @JsonProperty("Fuel Capacity")
    public void setFuelCapacity(String fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
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