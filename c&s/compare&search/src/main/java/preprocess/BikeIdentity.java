package preprocess;

import util.StringUtils;

import java.util.Comparator;

/**
 * @author bibek on 12/23/17
 * @project compare&search
 */
public class BikeIdentity implements Comparator<BikeIdentity>{
    private String companyName;
    private String bikeName;

    public BikeIdentity(String companyName, String bikeName) {
        this.companyName = StringUtils.lowerCaseWithNoWhiteSpace(companyName);
        this.bikeName = StringUtils.lowerCaseWithNoWhiteSpace(bikeName);
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getBikeName() {
        return bikeName;
    }

    @Override
    public int compare(BikeIdentity o1, BikeIdentity o2) {
        return o1.getBikeName().compareTo(o2.getBikeName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BikeIdentity that = (BikeIdentity) o;

        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        return bikeName != null ? bikeName.equals(that.bikeName) : that.bikeName == null;
    }

    @Override
    public int hashCode() {
        int result = companyName != null ? companyName.hashCode() : 0;
        result = 31 * result + (bikeName != null ? bikeName.hashCode() : 0);
        return result;
    }

}
