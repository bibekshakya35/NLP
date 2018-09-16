package deduplicator;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
public class DeduplicatorTest {
    @Test
    public void testRemoveDuplicates(){
        String[] contents = {
                "I love Emacs, since Emacs is awesome",
                "I do not love Emacs, since Emacs is not awesome",
                "Emacs is awesome, I love it",
                "Oh my way home. Long day ahead",
                "It's a long day, I have to admit it",
                "Good artists copy, great artists steal",
                "Something interesting, Good artists copy, great artists steal",
                "Great artists steal, Good artists copy",
                "I really think Emacs is awesome, and love it",
                "Emacs is loved by me as he is astounding",
                "Emacs's personality is amazing, I am attracted by him"
        };
        List<String> contentList = Arrays.asList(contents);

        Deduplication deDuplicator = new Deduplication(contentList, 0.5f);
        List<String> results = deDuplicator.dedup();

        assertEquals(7, results.size());
        assertEquals(contentList.get(0), results.get(0));
        assertEquals(contentList.get(2), results.get(1));
        assertEquals(contentList.get(3), results.get(2));
        assertEquals(contentList.get(4), results.get(3));
    }
}
