package topic;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.*;


import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class TopicModel {
    public static void main(String[] args) throws FileNotFoundException,
            UnsupportedEncodingException, IOException {
        Pattern patternForTokenSeq = Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}");


        //Begin by importing
        //documents from text
        //to feature sequences
        List<Pipe> pipeList = new ArrayList<>();
        pipeList.add(new CharSequenceLowercase());
        pipeList.add(new CharSequence2TokenSequence(patternForTokenSeq));
        pipeList.add(new TokenSequenceRemoveStopwords(
                new File(args[1]),
                "UTF-8",
                false,
                false,
                false));
        pipeList.add(new TokenSequence2FeatureSequence());

        InstanceList instances = new InstanceList(new SerialPipes(pipeList));
        Reader fileReader = new InputStreamReader(new FileInputStream(new File(args[0])), "UTF-8");
        Pattern pattern = Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$");
        CsvIterator csvIterator = new CsvIterator(fileReader,
                pattern,
                3, 2, 1);
        instances.addThruPipe(csvIterator);
        // create a model with 100 topics, alpha_t =0.01, beta_w=0.01
        // Note that the first parameter is passed as the sum over topics, while
        // the second is the parameter for a single dimension of the Dirichlet prior.
        int numTopics = 10;
        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);

        model.addInstances(instances);
        // Use two parallel samplers, which each look at one half the corpus and combine
        //  statistics after every iteration.
        model.setNumThreads(2);
        // Run the model for 50 iterations and stop (this is for testing only,
        //  for real applications, use 1000 to 2000 iterations)
        model.setNumIterations(50);
        model.estimate();
        //Show the words and topics in the first instance
        //the data alphabet maps word IDs to string
        Alphabet dataAlphabet = instances.getDataAlphabet();

        FeatureSequence tokens = (FeatureSequence) model.getData().
                get(0).instance
                .getData();
        LabelSequence topics = model.getData().get(0).topicSequence;
        Formatter formatter = new Formatter(new StringBuilder(), Locale.US);
        for (int position = 0; position < tokens.getLength(); position++) {
            formatter.format("%s-%d ",
                    dataAlphabet.lookupObject(tokens.getIndexAtPosition(position)),
                    topics.getIndexAtPosition(position));
        }
        System.out.println(formatter);
        //Estimate the topic distribution of the first instance
        //give the current Gibbs state
        double[] topicDistribution = model.getTopicProbabilities(0);
        //get an array of the sorted sets
        //of word ID/COUNT pairs
        ArrayList<TreeSet<IDSorter>> topicSortedWords
                = model
                .getSortedWords();
        //show top 5 words in topics
        //with proportions for the first document
        for (int topic = 0; topic < numTopics; topic++) {
            Iterator<IDSorter> iterator = topicSortedWords.get(topic)
                    .iterator();
            formatter = new Formatter(
                    new StringBuilder(), Locale.US
            );
            formatter.format("%d\t%.3f\t", topic, topicDistribution[topic]);
            int rank = 0;
            while (iterator.hasNext() && rank < 10) {
                IDSorter idCountPair = iterator.next();
                formatter.format("%s (%.0f) ", dataAlphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
                rank++;
            }
            System.out.println(formatter);
        }
        // create a new instance with high probabilities of topic 0
        StringBuilder topicZeroText = new StringBuilder();
        Iterator<IDSorter> iterator = topicSortedWords.get(0).iterator();
        int rank = 0;
        while (iterator.hasNext() && rank < 5) {
            IDSorter idCountPart = iterator.next();
            topicZeroText.append(dataAlphabet.lookupObject(idCountPart.getID()) + " ");
            rank++;
        }
        // create a new instance named "text instance" with empty target
        // and source field
        InstanceList testing = new InstanceList(instances.getPipe());
        testing.addThruPipe(new Instance(topicZeroText.toString(), null, "test instance", null));

        TopicInferencer inference = model.getInferencer();
        double[] testProbabilities = inference.getSampledDistribution(testing.get(0), 10, 1, 5);
        System.out.println("0\t" + testProbabilities[0]);
    }
}
