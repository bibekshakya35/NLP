package com.prasnottar.query;

import net.didion.jwnl.JWNLException;
import opennlp.tools.coref.mention.JWNLDictionary;
import opennlp.tools.parser.Parse;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Identify the features of the question by leveraging Wordnet and using the parse of the question
 * and the question word (how, why, what, etc.) to output a context, which is the set
 * of features that can be passed to the model.  Can use Wordnet to examine what words are
 * also predictive by looking at the synsets.
 * <p>
 * It is a rule-based feature selector
 */
public class AnswerTypeContextGenerator {
    private static final Pattern falseHeadsPattern = Pattern.compile("^(name|type|kind|sort|form|one|breed|names|variety)$");
    private static final Pattern copulaPattern = Pattern.compile("^(is|are|'s|were|was|will)$");
    private static final Pattern queryWordPattern = Pattern.compile("^(who|what|when|where|why|how|whom|which|name)$");
    private static final Pattern useFocusNounPattern = Pattern.compile("^(who|what|which|name)$");
    private static final Pattern howModifierTagPattern = Pattern.compile("^(JJ|RB)");
    private JWNLDictionary wordnet;

    /**
     * @param dictDir The dict directory under the wordnet dir
     */
    public AnswerTypeContextGenerator(File dictDir) {
        try {
            wordnet = new JWNLDictionary(dictDir.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Unable to initalize", e);
        } catch (JWNLException e) {
            throw new RuntimeException("Unable to initalize", e);
        }
    }

    private boolean isQueryWord(String word) {
        return (queryWordPattern.matcher(word).matches());
    }

    private int movePastPrep(int i, Parse[] toks) {
        if (i < toks.length && (toks[i].toString().equals("of") || toks[i].toString().equals("for"))) {
            i++;
        }
        return (i);
    }

    private int movePastOf(int i, Parse[] toks) {
        if (i < toks.length && toks[i].toString().equals("of")) {
            i++;
        }
        return (i);
    }

    //copula
    //a word used to link the subject of a sentence with a predicate
    private int movePastCopula(int i, Parse[] toks) {
        if (i < toks.length && toks[i].getType().startsWith("V")) {
            if (copulaPattern.matcher(toks[i].toString()).matches()) {
                i++;
            }
        }
        return (i);
    }

    private Parse[] getNounPhrases(Parse parse) {
        List<Parse> nps = new ArrayList<>();
        List<Parse> parts = new ArrayList<>();
        parts.add(parse);
        while (parts.size() > 0) {
            List<Parse> newParts = new ArrayList<>();
            for (int pi = 0; pi < parts.size(); pi++) {
                Parse cp = parts.get(pi);
                if (cp.getType().equals("NP") && cp.isFlat()) {
                    nps.add(cp);
                } else if (!cp.isPosTag()) {
                    newParts.addAll(Arrays.asList(cp.getChildren()));
                }
            }
            parts = newParts;
        }
        return nps.toArray(new Parse[nps.size()]);
    }

    private Parse getContainingNounPhrase(Parse token) {
        Parse parent = token.getParent();
        if (parent.getType().equals("NP")) {
            return parent;
        }
        return null;
    }

    private int getTokenIndexFollowingPhrase(Parse p, Parse[] toks) {
        Parse[] ptok = p.getTagNodes();
        Parse lastToken = ptok[ptok.length - 1];
        for (int ti = 0, tl = toks.length; ti < tl; ti++) {
            if (toks[ti] == lastToken) {
                return (ti + 1);
            }
        }
        return (toks.length);
    }

    public Parse findFocusNounPhrase(String queryWord, int qwi, Parse[] toks) {
        if (queryWord.equals("who")) {
            int npStart = movePastCopula(qwi + 1, toks);
            if (npStart > qwi + 1) {
                //check to ensure there is copula
                Parse np = getContainingNounPhrase(toks[npStart]);
                if (np != null) {
                    return (np);
                }
            }
        } else if (queryWord.equals("what")) {
            int npStart = movePastCopula(qwi + 1, toks);
            Parse np = getContainingNounPhrase(toks[npStart]);
            //remove copula case
            if (np != null) {
                Parse head = np.getHead();
                if (falseHeadsPattern.matcher(head.toString()).matches()) {
                    npStart += np.getChildren().length;
                    int np2Start = movePastPrep(npStart, toks);
                    if (np2Start > npStart) {
                        Parse snp = getContainingNounPhrase(toks[np2Start]);
                        if (snp != null) {
                            return (snp);
                        }
                    }
                }
                return (np);
            }
        } else if (queryWord.equals("which")) {
            //check fot false query words like which VBD
            int npStart = movePastCopula(qwi + 1, toks);
            if (npStart > qwi + 1) {
                return (getContainingNounPhrase(toks[npStart]));
            } else {
                npStart = movePastOf(qwi + 1, toks);
                return (getContainingNounPhrase(toks[npStart]));
            }
        } else if (queryWord.equals("how")) {
            if (qwi + 1 < toks.length) {
                return (getContainingNounPhrase(toks[qwi + 1]));
            }
        } else if (qwi == 0 && queryWord.equals("name")) {
            int npStart = qwi + 1;
            Parse np = getContainingNounPhrase(toks[npStart]);
            if (np != null) {
                Parse head = np.getHead();
                if (falseHeadsPattern.matcher(head.toString()).matches()) {
                    npStart += np.getChildren().length;
                    int np2Start = movePastPrep(npStart, toks);
                    if (np2Start > npStart) {
                        Parse snp = getContainingNounPhrase(toks[np2Start]);
                        if (snp != null) {
                            return (snp);
                        }
                    }
                }
                return (np);
            }
        }
        return (null);
    }

    private String[] getLemmas(Parse np) {
        //make sure we are getting a single word
        String word = np.getHead().toString().toLowerCase();
        return wordnet.getLemmas(word, "NN");
    }

    private Set<String> getSynsetSet(Parse np) {
        Set<String> synnset = new HashSet<>();
        String[] lemmas = getLemmas(np);
        for (int li = 0; li < lemmas.length; li++) {
            String[] synsets = wordnet.getParentSenseKeys(lemmas[li], "NN", 0);
            for (int si = 0; si < synsets.length; si++) {
                synnset.add(synsets[li]);
            }
        }
        return (synnset);
    }

    private void generateWorkNetFeatures(Parse focusNoun, List<String> features) {
        Parse[] toks = focusNoun.getTagNodes();
        if (toks[toks.length - 1].getType().startsWith("NN")) {
            return;
        }
        //check wordnet
        Set<String> synsets = getSynsetSet(focusNoun);
        for (String synset : synsets) {
            features.add("s=" + synset);
        }
    }

    private void generateWordFeatures(Parse focusNoun, List<String> features) {
        Parse[] toks = focusNoun.getTagNodes();
        int nsi = 0;
        for (; nsi < toks.length - 1; nsi++) {
            features.add("mv=" + toks[nsi]);
            features.add("mt=" + toks[nsi].getType());
        }
        features.add("hw=" + toks[nsi]);
        features.add("ht=" + toks[nsi].getType());
    }

    public String[] getContext(Parse query) {
        Parse focusNoun = null;
        String queryWord = null;
        List<String> features = new ArrayList<>();
        features.add("def");
        Parse[] nps = getNounPhrases(query);
        Parse[] toks = query.getTagNodes();
        int fnEnd = 0;
        int i = 0;
        boolean fnIsLast = false;
        for (; i < toks.length; i++) {
            String tok = toks[i].toString().toLowerCase();
            if (isQueryWord(tok)) {
                queryWord = tok;
                focusNoun = findFocusNounPhrase(queryWord, i, toks);
                if (tok.equals("how") && i + 1 < toks
                        .length) {
                    if (howModifierTagPattern.matcher(toks[i + 1].getType()).matches()) {
                        queryWord = tok + "_" + toks[i + 1].toString();
                    }
                }
                if (focusNoun != null) {
                    fnEnd = getTokenIndexFollowingPhrase(focusNoun, toks);
                }
                if (focusNoun != null && focusNoun.equals(nps.length - 1)) {
                    fnIsLast = true;
                }
                break;
            }
        }
        int ri = i + 1;
        if (focusNoun != null) {
            ri = fnEnd + 1;
        }
        for (; ri < toks.length; ri++) {
            features.add("rw=" + toks[ri].toString());
        }
        if (queryWord != null) {
            features.add("qw=" + queryWord);
            String verb = null;
            //skip first verb for some query words like how much
            for (int vi = i + 1; vi < toks.length; vi++) {
                String tag = toks[vi].getType();
                if (tag != null && tag.startsWith("V")) {
                    verb = toks[vi].getType();
                    break;
                }
            }
            if (focusNoun == null) {
                features.add("qw_verb=" + queryWord + "_" + verb);
                features.add("verb=" + verb);
                features.add("fn=null");
            } else if (useFocusNounPattern.matcher(queryWord).matches()) {
                generateWordFeatures(focusNoun, features);
                generateWorkNetFeatures(focusNoun, features);
            }
            if (fnIsLast) {
                features.add("fnIsLast=" + fnIsLast);
            }
        }
        return (features.toArray(new String[features.size()]));

    }
}
