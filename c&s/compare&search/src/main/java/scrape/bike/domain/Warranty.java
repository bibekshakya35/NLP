package scrape.bike.domain;

/**
 * @author bibek on 12/17/17
 * @project compare&search
 */
public class Warranty {
    private String yearWarranty;
    private String kiloMeterWarranty;

    public String getYearWarranty() {
        return yearWarranty;
    }

    public void setYearWarranty(String yearWarranty) {
        this.yearWarranty = yearWarranty;
    }

    public String getKiloMeterWarranty() {
        return kiloMeterWarranty;
    }

    public void setKiloMeterWarranty(String kiloMeterWarranty) {
        this.kiloMeterWarranty = kiloMeterWarranty;
    }
}
