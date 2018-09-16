package keywordextraction;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Keywords {
    private List<String> keywords;

    public Keywords() {
        this.keywords = new ArrayList<>();
    }

    public void add(String word) {
        if (StringUtils.isNotBlank(word)) {
            keywords.add(word);
        }
    }

    public List<String> getKeywords() {
        return keywords;
    }
}
