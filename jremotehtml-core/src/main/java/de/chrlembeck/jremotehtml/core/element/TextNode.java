package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import de.chrlembeck.jremotehtml.core.change.Change;

public class TextNode implements HTMLElement {

    private String text;

    private Tag parent;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public void render(Writer writer) throws IOException {
        writer.append(text);
    }

    @Override
    public void collectListeners(List<Change> listeners) {
    }

    public Tag getParent() {
        return parent;
    }

    @Override
    public void setParent(Tag parent) {
        this.parent = parent;
    }

    @Override
    public boolean isNewNode() {
        if (parent == null || parent.isNewNode()) {
            return true;
        }
        return getParent().getPage().isNewTextNode(this);
    }

    @Override
    public void unsetId() {
        // nothing to do here
    }
}