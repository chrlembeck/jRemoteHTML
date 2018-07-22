package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import de.chrlembeck.jremotehtml.core.change.Change;

public class TextNode implements HTMLElement {

    private static final long serialVersionUID = 344892465914416914L;

    public static final String SEPARATOR = "|";

    public static final String ESCAPE = "\\";

    private String text;

    private Tag parent;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public void render(Writer writer) throws IOException {
        writer.append(escape(text));
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
    public void unsetIds() {
        // nothing to do here
    }

    public void setText(String newText) {
        this.text = newText;
        if (!isNewNode()) {
            getParent().getPage().registerModifiedTextNode(this);
        }
    }

    public String getText() {
        return text;
    }

    public static String escape(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            switch (current) {
            case '\\':
                sb.append("\\\\");
                break;
            case '|':
                sb.append("\\|");
                break;
            default:
                sb.append(current);
            }
        }
        return sb.toString();
    }
}