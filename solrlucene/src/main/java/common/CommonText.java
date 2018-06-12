package common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author bibek on 1/7/18
 * @project tamingtext
 */
public class CommonText {
    public static List<String> listOfStringFromFile(){
        File dir = new File("/home/bibek/bs-workspace/tamingtext_resources/files");
        File [] files = dir.listFiles();

        List<String> list = new ArrayList<>();
        commonFileReader(files,list);
        return list;
    }
    public static List<String> provideSentenceForDetection(){
        File dir = new File("/home/bibek/bs-workspace/tamingtext_resources/files/SD");
        File [] files = dir.listFiles();
        List<String> list = new ArrayList<>();
        commonFileReader(files,list);
        return list;
    }
    public static List<String> provideSentenceParser(){
        File dir = new File("/home/bibek/bs-workspace/tamingtext_resources/files/SP");
        File [] files = dir.listFiles();
        List<String> list = new ArrayList<>();
        commonFileReader(files,list);
        return list;
    }
    private static void commonFileReader(File[] files,List<String> list){
        try{
            for (File file:
                    files) {
                Scanner sc = new Scanner(file);
                while (sc.hasNext()){
                    String line = sc.nextLine();
                    list.add(line);
                }
            }
        }catch (FileNotFoundException ex){
            System.err.println("File is unable to found");
        }
    }
}
