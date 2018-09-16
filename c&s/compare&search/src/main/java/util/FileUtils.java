package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bibek on 12/20/17
 * @project compare&search
 */
public class FileUtils extends org.apache.commons.io.FileUtils{
    public static List<File> listOfFileFromDirectory(String filePath){
        List<File> files = new ArrayList<>();
        File bikeFolder = new File(filePath);
        File[] bikeFiles = bikeFolder.listFiles();
        for (File file:
                bikeFiles) {
            if(file.isFile()){
                files.add(file);
            }
        }
        return files;
    }
    public static <T> void downloadFile(List<T> list,String fileName){
        try {
            org.apache.commons.io.FileUtils.writeStringToFile(new File(fileName), JsonUtils.toJsonList(list), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
