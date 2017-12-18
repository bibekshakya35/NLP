package scrape.bike.domain;

/**
 * @author bibek on 12/6/17
 * @project compare&search
 */
public class Tyre {
    private String frontSize;
    private String rearSize;
    private String type;

    public String getFrontSize() {
        return frontSize;
    }

    public void setFrontSize(String frontSize) {
        this.frontSize = frontSize;
    }

    public String getRearSize() {
        return rearSize;
    }

    public void setRearSize(String rearSize) {
        this.rearSize = rearSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
