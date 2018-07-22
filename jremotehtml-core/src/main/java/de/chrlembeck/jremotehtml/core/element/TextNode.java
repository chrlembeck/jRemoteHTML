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
        escape(text, writer);

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

    public static void escape(String text, Writer writer) throws IOException {
        // ersetzt alle \ durch \\ und alle | durch \|, so dass sie auf der
        // client-seite wieder erkannt werden können.
        // Dies ist notwendig, damit aufeinanderfolgende Text Nodes durch |
        // voneinander getrennt werden können.
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            switch (current) {
            case '\\':
                writer.append("\\\\");
                break;
            case '|':
                writer.append("\\|");
                break;
            default:
                writer.append(current);
            }
        }
    }
}