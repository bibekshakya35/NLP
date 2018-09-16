package spec.carrot2;

import org.carrot2.clustering.lingo.LingoClusteringAlgorithm;
import org.carrot2.core.*;
import org.junit.Test;
import spec.TamingTextTestJ4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Carrot2ExampleTest extends TamingTextTestJ4 {
    private static final String[] titles = {
            "Red Fox jumps over Lazy Brown Dogs",
            "Mary Loses Little Lamb.  Wolf At Large.",
            "Lazy Brown Dogs Promise Revenge on Red Fox",
            "March Comes in like a Lamb"
    };
    private static final String[] snippets = {
            "The sly red fox ran down the canyon, through the woods and over the field, jumping over Farmer Ted's lazy brown dogs as if they were two fallen trees.",
            "In a disastrous turn of events, Mary, the shepherd, lost one of her lambs last night between 10 and 11 PM.  While it can't be proved just yet, the strange disappearance of Mr. Wolf suggests his involvement.",
            "After being thoroughly embarrassed by the red fox yesterday, Farmer Ted's brown dogs came out with a press release vowing vengeance on the fox.",
            "After a cold and blustery February, citizens of Minneapolis were relieved that March entered like a lamb instead of a lion."
    };
    protected List<Document> documentList;

    @Test
    public void testSimple() {
        final Controller controller =
                ControllerFactory.createSimple();
        documentList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Document doc = new Document(titles[i], snippets[i], "file://foo_" + i + ".txt");
            documentList.add(doc);
        }
        final ProcessingResult result = controller.process(documentList, "red fox", LingoClusteringAlgorithm.class);
        displayResults(result);
    }

    private void displayResults(ProcessingResult result) {
        Collection<Cluster> clusters = result.getClusters();
        for (Cluster cluster : clusters) {
            System.out.println("Cluster : " + cluster.getLabel());
            for (Document document : cluster.getDocuments()) {
                System.out.println("\t " + document.getTitle());
            }
        }
    }

}
