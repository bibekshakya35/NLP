package keywordextraction;

import java.io.IOException;

public class KEStopList {
    private KEStopwords stopwords;

    public KEStopList() {
        this.stopwords = new KEStopwords();
    }

    public KEStopList generateStopwords(FileUtil fileUtil) throws IOException {
        final FileUtil fileUtilIterator = fileUtil.iterator();
        while (fileUtil.hasNext()) {
            stopwords.add(fileUtilIterator.next());
        }
        return this;
    }

    public KEStopwords getStopwords() {
        return stopwords;
    }
}
