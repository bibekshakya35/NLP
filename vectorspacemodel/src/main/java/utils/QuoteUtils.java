package utils;

import model.Quote;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author bibek on 1/13/18
 * @project vectorspacemodel
 */
public class QuoteUtils {
    public static List<Quote> produceList(){
        File files = new File("/home/bibek/bs-workspace/tamingtext_resources/files/quote");
        File[] fileArr = files.listFiles();
        List<Quote> qouteList = new ArrayList<>();
         for (File file
                 :fileArr){
            try{
                InputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder paragraphBuilder = new StringBuilder(2048);
                while((line =reader.readLine())!=null){
                    if(!line.startsWith("--")){
                       paragraphBuilder.append(line);
                    }
                    else{
                        qouteList.add(new Quote(new Random().nextInt(),paragraphBuilder.toString()));
                        paragraphBuilder.setLength(0);
                        continue;
                    }
                }

            }catch (Exception ex){
                System.err.println("Unable to found "+file.getName());
            }

        }
        return qouteList;
    }
}
