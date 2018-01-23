package keywordextraction;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

public class FileUtil implements Iterator<String> {
    private String filePath;
    private LineIterator iterator;

    public FileUtil(String filePath) {
        this.filePath = filePath;
    }

    private boolean isFilePathEmpty() {
        if (filePath == null) return true;
        filePath = filePath.trim();
        return filePath.isEmpty();
    }

    private File getFile() {
        ClassLoader thisClassLoader = getClass().getClassLoader();
        URL fileUrl = this.isFilePathEmpty() ? thisClassLoader.getResource("FoxStoplist.txt") :
                thisClassLoader.getResource(filePath);
        return new File(fileUrl.getFile());
    }

    private File getUserProvideFile() {
        ClassLoader thisClassLoader = getClass().getClassLoader();
        URL fileUrl = thisClassLoader.getResource(filePath);
        return new File(fileUrl.getFile());
    }

    /**
     * Note: Close iterator if it not closed
     *
     * @return
     * @throws IOException
     */
    public FileUtil iterator() throws IOException {
        if (iterator != null) closeIterator();
        iterator = FileUtils.lineIterator(getFile(), "UTF-8");
        return this;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = iterator.hasNext();
        if (!hasNext) closeIterator();
        return hasNext;

    }

    @Override
    public String next() {
        return iterator.nextLine();
    }

    private void closeIterator() {
        LineIterator.closeQuietly(iterator);
        iterator = null;
    }
}
