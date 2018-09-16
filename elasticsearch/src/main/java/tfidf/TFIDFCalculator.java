package tfidf;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bibek on 12/19/17
 * @project elasticsearch
 */
public class TFIDFCalculator {
    public double calculateTermFrequency(List<String> doc,String term){
        double result = 0;
        return result;
    }
    public void countWordOccurence(){
        List<String> list = Arrays.asList(
                "hello", "bye", "ciao", "bye", "ciao");
        Map<String,Integer> counts  = list.parallelStream()
                .collect(Collectors.toConcurrentMap(
                        w->w,w->1,Integer::sum
                ));
        System.out.println(counts);
    }
}
