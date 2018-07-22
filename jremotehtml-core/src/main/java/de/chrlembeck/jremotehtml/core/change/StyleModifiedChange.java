package de.chrlembeck.jremotehtml.core.change;

public class StyleModifiedChange implements Change {

    private String value;

    private int elementId;

    private String key;

    public StyleModifiedChange(int elementId, String key, String value) {
        this.elementId = elementId;
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public int getElementId() {
        return elementId;
    }
}