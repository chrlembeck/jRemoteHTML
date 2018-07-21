package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.HTMLElement;
import de.chrlembeck.jremotehtml.core.element.Tag;

public class InsertTagChange implements Change {

    public static final int APPEND_NODE = -1;

    private Tag parentTag;

    private HTMLElement newChild;

    private int position = APPEND_NODE;

    public InsertTagChange(Tag parentTag, HTMLElement newChild, int position) {
        this.parentTag = parentTag;
        this.newChild = newChild;
        this.position = position;
    }

    public Tag getParentTag() {
        return parentTag;
    }

    public HTMLElement getNewChild() {
        return newChild;
    }

    public int getPosition() {
        return position;
    }
}