package nlp.fuzzystring.trie;

import java.util.Map;

public class Trie {
    private TrieNode root;

    public Trie() {
        this.root = new TrieNode();
    }
    //insert a words into the trie
    public void insert(String word){
        Map<Character,TrieNode> children = root.children;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            TrieNode t;
            if (children.containsKey(c)){
                t = children.get(c);
            }else{
                t= new TrieNode(c);
                children.put(c,t);
            }
            children = t.children;
            //set leaf node
            if (i==word.length()-1){
                t.isLeaf =true;
            }
        }
    }
    //returns if the word is in th trie
    public boolean search(String word){
        TrieNode t = searchNode(word);
        if(t!=null&&t.isLeaf)
            return true;
        else
            return false;
    }
    //Return if there is any word in trie
    //that starts with the given prefix
    public boolean startsWith(String prefix){
        if (searchNode(prefix)==null){
            return false;
        }
        else
            return true;
    }

    private TrieNode searchNode(String word) {
        Map<Character,TrieNode> children = root.children;
        TrieNode t =null;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (children.containsKey(c)){
                t = children.get(c);
                children = t.children;
            }else{
                return null;
            }
        }
        return t;
    }
}
