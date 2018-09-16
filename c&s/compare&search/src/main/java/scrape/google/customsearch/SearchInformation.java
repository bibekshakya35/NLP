package scrape.google.customsearch;

/**
 * @author bibek on 11/27/17
 * @project compare&search
 */
public class SearchInformation {
    private float searchTime;
    private String totalResults;

    public SearchInformation() {

    }

    public float getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(float searchTime) {
        this.searchTime = searchTime;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
