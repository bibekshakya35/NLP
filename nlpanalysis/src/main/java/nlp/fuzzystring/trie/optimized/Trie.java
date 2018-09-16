package nlp.fuzzystring.trie.optimized;

public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }
    //insert a word into trie
    public void insert(String word){
        TrieNode p = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c-'a';
            if (p.arr[index]==null){
                TrieNode temp = new TrieNode();
                p.arr[index] = temp;
                p =temp;
            }else{
                p=p.arr[index];
            }
        }
        p.isEnd = true;
    }
    //return if the word is in the trie
    public boolean search(String word){
        TrieNode p = searchNode(word);
        if (p==null)return false;
        else return true;
    }

    private TrieNode searchNode(String word) {
        TrieNode p = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int index = c-'a';
            if (p.arr[index]!=null){
                p = p.arr[index];
            }
            else return null;
        }
        if (p==root)return null;
        return p;
    }
    public boolean startsWith(String prefix){
        TrieNode p =searchNode(prefix);
        return p!=null;
    }
}
