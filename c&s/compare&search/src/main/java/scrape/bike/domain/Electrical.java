package scrape.bike.domain;

/**
 * @author bibek on 12/17/17
 * @project compare&search
 */
public class Electrical {
    private String battery;
    private String ledTailLight;
    private String headLamp;

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getLedTailLight() {
        return ledTailLight;
    }

    public void setLedTailLight(String ledTailLight) {
        this.ledTailLight = ledTailLight;
    }

    public String getHeadLamp() {
        return headLamp;
    }

    public void setHeadLamp(String headLamp) {
        this.headLamp = headLamp;
    }
}
