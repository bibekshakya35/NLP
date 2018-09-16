package scrape.google.customsearch;

import java.util.List;

/**
 * @author bibek on 11/27/17
 * @project compare&search
 */
public class Result {
    private SearchInformation searchInformation;
    private List<Item> items;

    public Result() {

    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public SearchInformation getSearchInformation() {
        return searchInformation;
    }

    public void setSearchInformation(SearchInformation searchInformation) {
        this.searchInformation = searchInformation;
    }
}
