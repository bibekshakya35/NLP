package com.prasnottar.query;

public interface PrasnottarParam {
    public static final String PRASNOTTAR_PREFIX = "pras.";
    /**
     * The Size of the Passage window around a match, measured in Tokens
     */
    public static final String PRIMARY_WINDOW_SIZE = PRASNOTTAR_PREFIX + "pws";

    public static final String ADJACENT_WINDOW_SIZE = PRASNOTTAR_PREFIX + "aws";

    public static final String SECONDARY_WINDOW_SIZE = PRASNOTTAR_PREFIX + "sws";

    public static final String QUERY_FIELD = PRASNOTTAR_PREFIX + "qf";

    public static final String OWL_ROWS = PRASNOTTAR_PREFIX + "rows";

    public static final String SLOP = PRASNOTTAR_PREFIX + "qSlop";

    public static final String BIGRAM_WEIGHT = PRASNOTTAR_PREFIX + "bw";

    public static final String ADJACENT_WEIGHT = PRASNOTTAR_PREFIX + "aw";

    public static final String SECOND_ADJ_WEIGHT = PRASNOTTAR_PREFIX + "saw";

    public static final String COMPONENT_NAME = "prasnottar";
}