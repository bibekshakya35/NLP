package fuzzystring.prefix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TrieNode {


    private boolean isWord;
    private TrieNode[] children;
    private String suffix;

    public TrieNode(boolean word, String suffix) {
        this.isWord = word;
        if (suffix == null) children = new TrieNode[26];
        this.suffix = suffix;
    }


    public TrieNode(boolean word) {
        this(word,null);
    }

    public boolean isWord() {
        return isWord;
    }


    public boolean addWord(String word) {
        return addWord(word.toLowerCase(),0);
    }

    private boolean addWord(String word, int index) {
        if (index == word.length()) {
            if (isWord) {
                return false;
            }
            else {
                isWord = true;
                return true;
            }
        }
        if (suffix != null) {
            if (suffix.equals(word.substring(index))) {
                return false;
            }
            String tmp = suffix;
            this.suffix = null;
            children = new TrieNode[26];
            addWord(tmp,0);
        }
        int ci = word.charAt(index)-(int)'a';
        TrieNode child = children[ci];
        if (child == null) {
            if (word.length() == index -1) {
                children[ci] = new TrieNode(true,null);
            }
            else {
                children[ci] = new TrieNode(false,word.substring(index+1));
            }
            return true;
        }
        return child.addWord(word, index+1);
    }

    public String[] getWords(String prefix, int numWords) {
        List<String> words = new ArrayList<String>(numWords);
        TrieNode prefixRoot = this;
        for (int i=0;i<prefix.length();i++) {
            if (prefixRoot.suffix == null) {
                    
                int ci = prefix.charAt(i)-(int)'a';
                prefixRoot = prefixRoot.children[ci];
                if (prefixRoot == null) {
                    break;
                }
            }
            else {
                if (prefixRoot.suffix.startsWith(prefix.substring(i))) {
                    words.add(prefix.substring(0,i)+prefixRoot.suffix);
                }
                prefixRoot = null;
                break;
            }
        }
        if (prefixRoot != null) {
            prefixRoot.collectWords(words,numWords,prefix);
        }
        return words.toArray(new String[words.size()]);
    }

    private void collectWords(List<String> words,
                              int numWords, String prefix) {
        if (this.isWord()) {
            words.add(prefix);
            if (words.size() == numWords) return;
        }
        if (suffix != null) {
            words.add(prefix+suffix);
            return;
        }
        for (int ci=0;ci<children.length;ci++) {
            String nextPrefix = prefix+(char) (ci+(int)'a');
            if (children[ci] != null) {
                children[ci].collectWords(words, numWords, nextPrefix);
                if (words.size() == numWords) return;
            }
        }
    }


    public String toString() {
        StringBuffer cs = new StringBuffer(children.length);
        for (int ci=0;ci<children.length;ci++) {
            if (children[ci] != null) {
                cs.append((char) (ci+(int)'a'));
            }
        }
        return "word="+isWord+" suffix="+suffix+" cs="+cs;
    }

    public static void main(String[] args) throws IOException {
        TrieNode node = new TrieNode(false);
        int lc = 0;
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        for (String line = br.readLine();line !=null;line = br.readLine()) {
            node.addWord(line);
            lc++;
        }
        System.out.println("Loaded "+lc+" lines");
        BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
        for (String line = br2.readLine();line !=null;line = br2.readLine()) {
            String[] words = node.getWords(line, 10);
            System.out.println(java.util.Arrays.asList(words));
        }
    }
}


