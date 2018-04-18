package fuzzystring.trie.optimized;

class TrieNode {
    TrieNode[] arr;
    boolean isEnd;

    public TrieNode() {
        this.arr = new TrieNode[26];
    }
}
