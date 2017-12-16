package chapterone;

/**
 * @author bibek on 12/14/17
 * @project tamingtext
 */
public enum OpenNLPModel {
    //Trained on opennlp training data.
    //for sentence detector
    SentenceDetector("en-sent.bin"),
    //Person name finder model.
    PersonNameFinder("en-ner-person.bin"),
    // 	Date name finder model.
    DateNameFinder("en-ner-date.bin"),
    //Location name finder model
    LocationNameFinder("en-ner-location.bin")
    ;
    private final String modelFileName;

    OpenNLPModel(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public String getModelFileName() {
        return modelFileName;
    }
}
