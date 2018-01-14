package model;

/**
 * @author bibek on 1/13/18
 * @project vectorspacemodel
 */
public class Quote {
    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Quote(int id, String text) {
        this.id = id;
        this.text = text;
    }
}
