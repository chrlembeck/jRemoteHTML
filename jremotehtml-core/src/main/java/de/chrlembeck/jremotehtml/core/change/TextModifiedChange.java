package de.chrlembeck.jremotehtml.core.change;

public class TextModifiedChange implements Change {

    private int parentId;

    private int position;

    private String text;

    public TextModifiedChange(int parentId, int position, String text) {
        this.parentId = parentId;
        this.position = position;
        this.text = text;
    }

    public int getParentId() {
        return parentId;
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }
}