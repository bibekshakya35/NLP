package spec.fuzzystring.trie;

import fuzzystring.trie.Trie;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TrieSpec {
    private Trie trie;

    @Before
    public void init() {
        trie = new Trie();
        String keys[] = {"the", "a", "there", "answer", "any",
                "by", "bye", "their"};
        for (String key : keys) {
            trie.insert(key);
        }
    }

    @Test
    public void shouldSearchGivenWord() {
        Assert.assertEquals(trie.search("the"), true);
        Assert.assertEquals(trie.search("these"), false);
        Assert.assertEquals(trie.search("their"), true);
        Assert.assertEquals(trie.search("thew"), false);
    }

    @After
    public void destroy() {
        trie = null;
    }
}
