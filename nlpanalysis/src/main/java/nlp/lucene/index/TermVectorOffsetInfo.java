package nlp.lucene.index;
import java.io.Serializable;

public class TermVectorOffsetInfo implements Serializable {
    public static final transient TermVectorOffsetInfo[] EMPTY_OFFSET_INFO = new TermVectorOffsetInfo[0];
    private int startOffset;
    private int endOffset;

    public TermVectorOffsetInfo() {
    }

    public TermVectorOffsetInfo(int startOffset, int endOffset) {
        this.endOffset = endOffset;
        this.startOffset = startOffset;
    }

    public int getEndOffset() {
        return this.endOffset;
    }

    public void setEndOffset(int endOffset) {
        this.endOffset = endOffset;
    }

    public int getStartOffset() {
        return this.startOffset;
    }

    public void setStartOffset(int startOffset) {
        this.startOffset = startOffset;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof TermVectorOffsetInfo)) {
            return false;
        } else {
            TermVectorOffsetInfo termVectorOffsetInfo = (TermVectorOffsetInfo)o;
            if (this.endOffset != termVectorOffsetInfo.endOffset) {
                return false;
            } else {
                return this.startOffset == termVectorOffsetInfo.startOffset;
            }
        }
    }

    public int hashCode() {
        int result = this.startOffset;
        result = 29 * result + this.endOffset;
        return result;
    }
}
