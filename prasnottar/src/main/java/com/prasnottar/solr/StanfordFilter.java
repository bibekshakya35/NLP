package com.prasnottar.solr;

import edu.stanford.nlp.ling.CoreAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.time.TimeAnnotations;
import edu.stanford.nlp.time.TimeAnnotator;
import edu.stanford.nlp.time.TimeExpression;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.util.AttributeSource;

import java.io.IOException;
import java.util.*;

@Slf4j
public class StanfordFilter extends TokenFilter {
    private final CharTermAttribute termAttribute
            = addAttribute(CharTermAttribute.class);
    private OffsetAttribute offsetAttribute
            = addAttribute(OffsetAttribute.class);
    private TypeAttribute typeAttribute =
            addAttribute(TypeAttribute.class);
    private final PositionIncrementAttribute positionIncrementAttribute
            = addAttribute(PositionIncrementAttribute.class);
    private StanfordCoreNLP pipeline;
    int tokenOffset, start, end;
    public static final String NE_PREFIX = "NE_";
    private final KeywordAttribute keywordAttribute
            = addAttribute(KeywordAttribute.class);
    private Queue<TokenData> tokenDataQueue = new LinkedList<>();
    private final Queue<AttributeSource.State> stateQueue
            = new LinkedList<>();
    private Queue<TimeData> timeQueue =
            new LinkedList<>();
    private Iterator<TokenData> iterator = null;
    private Iterator<TokenData> iterator_cpy = null;

    public StanfordFilter(TokenStream tokenStream, StanfordCoreNLP pipeline) {
        super(tokenStream);
        log.info("Inside filter");
        this.pipeline = pipeline;
    }

    public Iterator findTokens() throws IOException {
        if (!input.incrementToken()) return null;
        String text;
        text = input.getAttribute(CharTermAttribute.class).toString();
        //read some text in the text variable
        Annotation document = new Annotation(text);
        //these are all the sentences in the document
        //a CoreMap is essentially a Map that uses class objects
        //as a keys and has values with custom types
        pipeline.annotate(document);
        List<CoreMap> timexAnnsAll = document.get(TimeAnnotations.TimexAnnotations.class);
        timexAnnsAll.forEach(coreMap -> {
            List<CoreLabel> tokens = coreMap.get(CoreAnnotations.TokensAnnotation.class);
            TimeData timeData = new TimeData();
            timeData.setTime(coreMap.get(TimeExpression.Annotation.class).getTemporal().toString());
            timeData.setStart(tokens.get(0)
                    .get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
            timeData.setEnd(tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class));
            timeQueue.add(timeData);
        });
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        sentences.forEach(sentence -> {
            //traversing the words in the current sentence
            //a CoreLabel is a CoreMap with additional token specific methods
            sentence.get(CoreAnnotations.TokensAnnotation.class)
                    .forEach(token -> {
                        //text of the token
                        log.info("In token");
                        String word = token.get(CoreAnnotations.TextAnnotation.class);
                        //this is the pos tag of the token
                        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        //this is NER label of the token
                        String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                        log.info("Work as token {} work as pos (part of speech) {} " +
                                        "and as name entity recognition {} ",
                                new Object[]{
                                        word, pos, ner
                                });
                        TokenData tokenData = new TokenData();
                        tokenData.setNER(ner);
                        tokenData.setToken(word);
                        tokenData.setPOS(pos);
                        tokenDataQueue.add(tokenData);
                    });
        });
        Iterator<TokenData> tokenDataIterator = tokenDataQueue.iterator();
        iterator_cpy = tokenDataQueue.iterator();
        tokenOffset = 0;
        start = 0;
        end = 0;
        return iterator;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (stateQueue.peek() == null) {
            if (iterator == null) {
                iterator = findTokens();
            }
            TokenData tokenData;
            if (iterator.hasNext()) {
                return false;
            } else {
                tokenData = (TokenData) iterator.next();
                TokenData tok2 = null;
                if (iterator_cpy.hasNext())
                    tok2 = (TokenData) iterator_cpy.next();
                String word = tokenData.getToken();
                this.start = this.end;
                clearAttributes();
                end = start + word.length();
                offsetAttribute.setOffset(start, end);
                typeAttribute.setType("<ALPHANUM>");
                keywordAttribute.setKeyword(true);
                positionIncrementAttribute.setPositionIncrement(0);
                String pos = tokenData.getPOS();
                if (!pos.equals("-")) {
                    termAttribute.setEmpty().append(pos);
                    stateQueue.add(captureState());
                }
                if (!tokenData.getNER().equals("0")) {
                    positionIncrementAttribute.setPositionIncrement(0);
                    String ne = tokenData.getNER();
                    termAttribute.setEmpty().append(NE_PREFIX + ne);
                    stateQueue.add(captureState());
                    if (ne.equals("DATE")) {
                        int st = start, en = end;
                        if (tokenData == tok2) {
                            while (iterator_cpy.hasNext()) {
                                tok2 = (TokenData) iterator_cpy.next();
                                if (!tok2.getNER().equals("DATE")) {
                                    break;
                                }
                                st = en;
                                en = st + tok2.getToken().length();
                            }
                            TimeData tm = timeQueue.poll();
                            positionIncrementAttribute.setPositionIncrement(0);
                            keywordAttribute.setKeyword(true);
                            String timeData = tm.getTime();
                            termAttribute.setEmpty().append(timeData);
                            offsetAttribute.setOffset(start, en);
                            typeAttribute.setType("<ALPHANUM>");
                            stateQueue.add(captureState());
                        }
                    }
                }
                keywordAttribute.setKeyword(true);
                offsetAttribute.setOffset(start, end);
                typeAttribute.setType("<ALPHANUM>");
                termAttribute.setEmpty().append(word);
                positionIncrementAttribute.setPositionIncrement(1);
                tokenOffset++;
                return true;
            }
        }
        clearAttributes();
        State state = stateQueue.poll();
        restoreState(state);
        return false;
    }

    @Override
    public void end() throws IOException {
        super.end();
        positionIncrementAttribute.setPositionIncrement(positionIncrementAttribute.getPositionIncrement() + 1);
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        tokenDataQueue.clear();
        timeQueue.clear();
        stateQueue.clear();
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, cleanxml, ssplit,pos,lemma,ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);
        pipeline.addAnnotator(new TimeAnnotator("sutime", properties));

        String text = "<mydata> refeer</mydata>today is 12 jan 2016. what is tommorow? Who is Avtar? Does he work at Apple or Google? Sumit was born on 13 feb,2011.";
        Annotation document = new Annotation(text);
        pipeline.annotate(document);
        log.info(document.get(CoreAnnotations.TextAnnotation.class));
        List<CoreMap> timexAnnsAll = document.get(TimeAnnotations.TimexAnnotations.class);
        timexAnnsAll.forEach(timex -> {
            List<CoreLabel> tokens = timex.get(CoreAnnotations.TokensAnnotation.class);
            TimeData timeData = new TimeData();
            timeData.setTime(timex.get(TimeExpression.Annotation.class).getTemporal().toISOString());
            timeData.setStart(tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class));
            timeData.setEnd(tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class));
        });
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        sentences.forEach(sentence -> {
            //traversing the words in the current sentence
            //a CoreLabel is a CoreMap with additional token specific methods
            sentence.get(CoreAnnotations.TokensAnnotation.class)
                    .forEach(token -> {
                        //text of the token
                        String word = token.get(CoreAnnotations.TextAnnotation.class);
                        //this is the pos tag of the token
                        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                        //this is NER label of the token
                        String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                        log.info("token : {}\n pos  : {}\n " +
                                        "and name entity recognition : {}\n ",
                                new Object[]{
                                        word, pos, ner
                                });

                    });
        });
    }
}
