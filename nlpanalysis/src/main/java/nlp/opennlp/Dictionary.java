package nlp.opennlp;

public interface Dictionary {
    String[] getLemmas(String var1, String var2);

    String getSenseKey(String var1, String var2, int var3);

    int getNumSenses(String var1, String var2);

    String[] getParentSenseKeys(String var1, String var2, int var3);
}
