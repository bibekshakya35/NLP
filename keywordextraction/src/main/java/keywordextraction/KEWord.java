package keywordextraction;

public class KEWord {
    private String value;

    public KEWord(String value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return value == null || value.trim().isEmpty();
    }

    public String getAsLowerCase() {
        return value.trim().toLowerCase();
    }
}
