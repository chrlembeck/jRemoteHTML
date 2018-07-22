package de.chrlembeck.jremotehtml.core.change;

public class AttributeRemovedChange implements Change {

    private String key;

    private int elementId;

    public AttributeRemovedChange(int elementId, String key) {
        this.elementId = elementId;
        this.key = key;
    }

    public int getElementId() {
        return elementId;
    }

    public String getKey() {
        return key;
    }
}