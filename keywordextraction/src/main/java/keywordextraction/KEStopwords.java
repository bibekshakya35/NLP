package keywordextraction;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class KEStopwords {
    private List<String> words;

    public KEStopwords() {
        this.words = new ArrayList<>();
    }

    public void add(String line) {
        if (!line.startsWith("#")) {
            this.words.add(line);
        }
    }
    public Pattern getStopwordPattern(){
        final StringBuilder stopwordsPatternBuilder = new StringBuilder();
        for (final String stopword : words){
            stopwordsPatternBuilder.append("\\b");
            stopwordsPatternBuilder.append(stopword);
            stopwordsPatternBuilder.append("\\b");
            stopwordsPatternBuilder.append("|");
        }
        String stopwordsPatternStr  =stopwordsPatternBuilder.toString();
        String stopWordPatternStr = StringUtils.chop(stopwordsPatternStr);
        return Pattern.compile(stopWordPatternStr,Pattern.CASE_INSENSITIVE);
    }
}
