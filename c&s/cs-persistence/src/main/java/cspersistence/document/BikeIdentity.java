package cspersistence.document;

import util.StringUtils;

import java.util.Comparator;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public class BikeIdentity{
    private String companyName;
    private String bikeName;

    public BikeIdentity() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }
}
