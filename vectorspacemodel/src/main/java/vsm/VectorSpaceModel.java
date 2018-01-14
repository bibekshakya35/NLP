package vsm;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author bibek on 1/7/18
 * @project vectorspacemodel
 */
public class VectorSpaceModel {
    public Map<String,Long> buildDictionary() throws FileNotFoundException{
        Map<String,Long> dictionary = new HashMap<>();
        Scanner read2016File = new Scanner(new File("/home/bibek/2016.txt"));
        long indexed=0;
        while (read2016File.hasNext()){
            String word = read2016File.next();
            if(!dictionary.containsKey(word.toLowerCase())){
                dictionary.put(word,indexed);
                indexed++;
            }
        }
        return dictionary;
    }
}
