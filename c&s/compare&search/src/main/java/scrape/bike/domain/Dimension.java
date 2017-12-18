package scrape.bike.domain;

/**
 * @author bibek on 12/17/17
 * @project compare&search
 */
public class Dimension {
    private String lengthWidthHeight;
    private String groundClearance;
    private String kerbWeight;
    private String wheelBase;
    private String fuelCapacity;

    public String getLengthWidthHeight() {
        return lengthWidthHeight;
    }

    public void setLengthWidthHeight(String lengthWidthHeight) {
        this.lengthWidthHeight = lengthWidthHeight;
    }

    public String getGroundClearance() {
        return groundClearance;
    }

    public void setGroundClearance(String groundClearance) {
        this.groundClearance = groundClearance;
    }

    public String getKerbWeight() {
        return kerbWeight;
    }

    public void setKerbWeight(String kerbWeight) {
        this.kerbWeight = kerbWeight;
    }

    public String getWheelBase() {
        return wheelBase;
    }

    public void setWheelBase(String wheelBase) {
        this.wheelBase = wheelBase;
    }

    public String getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(String fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }
}
