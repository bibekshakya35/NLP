package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author bibek on 12/19/17
 * @project elasticsearch
 */
@State(Scope.Thread)
public class ParallelWordCount {
    @Param({
            "toConcurrentMap", "toMap", "groupingBy", "groupingByConcurrent"
    })
    public String method;
    @Param({
            "2", "5", "10"
    })
    public int wordLength;
    @Param({
            "1000", "10000"
    })
    public int count;
    private List<String> list;

    @Setup
    public void initList() {
        list = createRandomStrings(count, wordLength, new Random(0));
    }
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void testMethod(Blackhole bh){
        Map<String,Integer> counts;
        switch (method){
            case "toMap":

                counts= list.stream().collect(Collectors.toMap(
                            w->w,
                            w->1,
                            Integer::sum
                    ));
                bh.consume(counts);
                break;
            case "toConcurrentMap":
                counts =list.parallelStream().collect(
                        Collectors.toConcurrentMap(
                                w->w,
                                w->1,
                                Integer::sum
                        )
                );
                bh.consume(counts);
                break;
            case "groupingBy":
                Map<String, Long> countsLong = list.stream()
                        .collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.<String>counting()
                        ));
                bh.consume(countsLong);
                break;
            case "groupingByConcurrent":
                Map<String,Long> conter =
                        list.parallelStream().collect(Collectors.groupingByConcurrent(Function.identity(),
                                Collectors.<String>counting()));
                bh.consume(conter);
                break;

        }


    }

    private List<String> createRandomStrings(int count, int wordLength, Random random) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(createRandomString(wordLength, random));
        }
        return list;
    }

    private String createRandomString(int wordLength, Random random) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            int c = random.nextInt(26);
            sb.append((char) (c + 'a'));
        }
        return sb.toString();
    }

}
