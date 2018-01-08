package tokenization;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author bibek on 1/7/18
 * @project tamingtext
 */
public class CommonTokenization {
    public static List<String> listOfStringFromFile() throws FileNotFoundException{
        File dir = new File("/home/bibek/bs-workspace/filesToIndex");
        File [] files = dir.listFiles();

        List<String> list = new ArrayList<>();
        for (File file:
                files) {
            Scanner sc = new Scanner(file);
            while (sc.hasNext()){
                String line = sc.nextLine();
                list.add(line);
            }
        }
        return list;
    }
}
