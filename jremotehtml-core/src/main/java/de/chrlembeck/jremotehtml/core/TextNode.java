package de.chrlembeck.jremotehtml.core;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class TextNode implements HTMLElement {

    private String text;

    private Tag parent;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public void render(Page page, Writer writer) throws IOException {
        writer.append(text);
    }

    @Override
    public void collectListeners(List<Change> listeners) {
    }

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    @Override
    public boolean isNewNode() {
        if (parent == null || parent.isNewNode()) {
            return true;
        }

        // TODO FIXME ALERT Prüfung einbauen, ob der knoten wirklich neu ist.
        return false;
    }

    @Override
    public void unsetId() {
        // nothing to do here
    }
}