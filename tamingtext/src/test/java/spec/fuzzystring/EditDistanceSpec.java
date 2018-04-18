package spec.fuzzystring;

import fuzzystring.EditDistance;
import org.junit.Test;

public class EditDistanceSpec {
    @Test
    public void shouldlevenshteinDistance(){
        EditDistance editDistance = new EditDistance();
        int x = editDistance.levenshteinDistance("Tamming Text".toCharArray(),"Taming Test".toCharArray());
        System.out.println(x);
    }
}
