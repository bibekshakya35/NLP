package nlp.fuzzystring.trie;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for Trie (prefix tree)
 * an ordered tree data structure that is used to store a dynamic set or associative array where
 * key are usually strings
 */
class TrieNode {
    char c;
    Map<Character,TrieNode> children = new HashMap<>();
    boolean isLeaf;
    public TrieNode() {
    }

    public TrieNode(char c) {
        this.c = c;
    }
}
