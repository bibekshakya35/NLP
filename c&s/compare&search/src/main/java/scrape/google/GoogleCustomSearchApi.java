package scrape.google;
import static java.lang.String.format;
import static com.google.common.base.CharMatcher.whitespace;

/**
 * @author bibek on 11/27/17
 * @project compare&search
 */
public class GoogleCustomSearchApi {
    private String cx="014723624719242706501:ky6zn2teax4";
    private String apiKey="AIzaSyAwH-HNs_We3gqJI0N491XjYMuOI3vtvpA";
    private int num;
    private int start = 1;

    public String getUri(String query) {
        return format("https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s&start=%d&alt=json",
                apiKey,
                cx,
                whitespace().trimAndCollapseFrom(query, '+'),
                getStart());
    }

    public int getStart() {
        return start;
    }
}

