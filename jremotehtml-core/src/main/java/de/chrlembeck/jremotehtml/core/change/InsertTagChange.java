package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.HTMLDomNode;
import de.chrlembeck.jremotehtml.core.element.HTMLElement;

public class InsertTagChange implements Change {

    public static final int APPEND_NODE = -1;

    private HTMLElement parentTag;

    private HTMLDomNode newChild;

    private int position = APPEND_NODE;

    public InsertTagChange(HTMLElement parentTag, HTMLDomNode newChild, int position) {
        this.parentTag = parentTag;
        this.newChild = newChild;
        this.position = position;
    }

    public HTMLElement getParentTag() {
        return parentTag;
    }

    public HTMLDomNode getNewChild() {
        return newChild;
    }

    public int getPosition() {
        return position;
    }
}