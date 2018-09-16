package nlp.opennlp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.Adjective;
import net.didion.jwnl.data.FileDictionaryElementFactory;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.VerbFrame;
import net.didion.jwnl.dictionary.FileBackedDictionary;
import net.didion.jwnl.dictionary.MorphologicalProcessor;
import net.didion.jwnl.dictionary.file_manager.FileManager;
import net.didion.jwnl.dictionary.file_manager.FileManagerImpl;
import net.didion.jwnl.dictionary.morph.DefaultMorphologicalProcessor;
import net.didion.jwnl.dictionary.morph.DetachSuffixesOperation;
import net.didion.jwnl.dictionary.morph.LookupExceptionsOperation;
import net.didion.jwnl.dictionary.morph.LookupIndexWordOperation;
import net.didion.jwnl.dictionary.morph.Operation;
import net.didion.jwnl.dictionary.morph.TokenizerOperation;
import net.didion.jwnl.princeton.data.PrincetonWN17FileDictionaryElementFactory;
import net.didion.jwnl.princeton.file.PrincetonRandomAccessDictionaryFile;

public class JWNLDictionary implements Dictionary {
    private net.didion.jwnl.dictionary.Dictionary dict;
    private MorphologicalProcessor morphy;
    private static String[] empty = new String[0];

    public JWNLDictionary(String searchDirectory) throws IOException, JWNLException {
        PointerType.initialize();
        Adjective.initialize();
        VerbFrame.initialize();
        Map<POS, String[][]> suffixMap = new HashMap();
        suffixMap.put(POS.NOUN, new String[][]{{"s", ""}, {"ses", "s"}, {"xes", "x"}, {"zes", "z"}, {"ches", "ch"}, {"shes", "sh"}, {"men", "man"}, {"ies", "y"}});
        suffixMap.put(POS.VERB, new String[][]{{"s", ""}, {"ies", "y"}, {"es", "e"}, {"es", ""}, {"ed", "e"}, {"ed", ""}, {"ing", "e"}, {"ing", ""}});
        suffixMap.put(POS.ADJECTIVE, new String[][]{{"er", ""}, {"est", ""}, {"er", "e"}, {"est", "e"}});
        DetachSuffixesOperation tokDso = new DetachSuffixesOperation(suffixMap);
        tokDso.addDelegate("operations", new Operation[]{new LookupIndexWordOperation(), new LookupExceptionsOperation()});
        TokenizerOperation tokOp = new TokenizerOperation(new String[]{" ", "-"});
        tokOp.addDelegate("token_operations", new Operation[]{new LookupIndexWordOperation(), new LookupExceptionsOperation(), tokDso});
        DetachSuffixesOperation morphDso = new DetachSuffixesOperation(suffixMap);
        morphDso.addDelegate("operations", new Operation[]{new LookupIndexWordOperation(), new LookupExceptionsOperation()});
        Operation[] operations = new Operation[]{new LookupExceptionsOperation(), morphDso, tokOp};
        this.morphy = new DefaultMorphologicalProcessor(operations);
        FileManager manager = new FileManagerImpl(searchDirectory, PrincetonRandomAccessDictionaryFile.class);
        FileDictionaryElementFactory factory = new PrincetonWN17FileDictionaryElementFactory();
        FileBackedDictionary.install(manager, this.morphy, factory, true);
        this.dict = net.didion.jwnl.dictionary.Dictionary.getInstance();
        this.morphy = this.dict.getMorphologicalProcessor();
    }

    public String[] getLemmas(String word, String tag) {
        try {
            POS pos;
            if (!tag.startsWith("N") && !tag.startsWith("n")) {
                if (!tag.startsWith("V") && !tag.startsWith("v")) {
                    if (!tag.startsWith("J") && !tag.startsWith("a")) {
                        if (!tag.startsWith("R") && !tag.startsWith("r")) {
                            pos = POS.NOUN;
                        } else {
                            pos = POS.ADVERB;
                        }
                    } else {
                        pos = POS.ADJECTIVE;
                    }
                } else {
                    pos = POS.VERB;
                }
            } else {
                pos = POS.NOUN;
            }

            List<String> lemmas = this.morphy.lookupAllBaseForms(pos, word);
            return (String[])lemmas.toArray(new String[lemmas.size()]);
        } catch (JWNLException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public String getSenseKey(String lemma, String pos, int sense) {
        try {
            IndexWord iw = this.dict.getIndexWord(POS.NOUN, lemma);
            return iw == null ? null : String.valueOf(iw.getSynsetOffsets()[sense]);
        } catch (JWNLException var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public int getNumSenses(String lemma, String pos) {
        try {
            IndexWord iw = this.dict.getIndexWord(POS.NOUN, lemma);
            return iw == null ? 0 : iw.getSenseCount();
        } catch (JWNLException var4) {
            return 0;
        }
    }

    private void getParents(Synset synset, List<String> parents) throws JWNLException {
        Pointer[] pointers = synset.getPointers();
        int pi = 0;

        for(int pn = pointers.length; pi < pn; ++pi) {
            if (pointers[pi].getType() == PointerType.HYPERNYM) {
                Synset parent = pointers[pi].getTargetSynset();
                parents.add(String.valueOf(parent.getOffset()));
                this.getParents(parent, parents);
            }
        }

    }

    public String[] getParentSenseKeys(String lemma, String pos, int sense) {
        try {
            IndexWord iw = this.dict.getIndexWord(POS.NOUN, lemma);
            if (iw != null) {
                Synset synset = iw.getSense(sense + 1);
                List<String> parents = new ArrayList();
                this.getParents(synset, parents);
                return (String[])parents.toArray(new String[parents.size()]);
            } else {
                return empty;
            }
        } catch (JWNLException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException, JWNLException {
        String searchDir = System.getProperty("WNSEARCHDIR");
        System.err.println("searchDir=" + searchDir);
        if (searchDir != null) {
            Dictionary dict = new JWNLDictionary(System.getProperty("WNSEARCHDIR"));
            String word = args[0];
            String[] lemmas = dict.getLemmas(word, "NN");
            int li = 0;

            for(int ln = lemmas.length; li < ln; ++li) {
                int si = 0;

                for(int sn = dict.getNumSenses(lemmas[li], "NN"); si < sn; ++si) {
                    System.out.println(lemmas[li] + " (" + si + ")\t" + Arrays.asList(dict.getParentSenseKeys(lemmas[li], "NN", si)));
                }
            }
        }

    }
}
