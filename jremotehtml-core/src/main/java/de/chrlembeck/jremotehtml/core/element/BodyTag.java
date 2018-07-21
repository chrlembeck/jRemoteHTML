package de.chrlembeck.jremotehtml.core.element;

import de.chrlembeck.jremotehtml.core.change.Change;

public class BodyTag extends Tag {

    private Page page;

    public BodyTag(Page page) {
        super("body");
        this.page = page;
        setId(page.nextId());
        setAttribute("onload", "loadContent()");
    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    protected void notifyChange(Change change) {
        if (page != null) {
            page.changeHappened(change);
        }
    }

    @Override
    public boolean isNewNode() {
        // the body node is always known to the client
        return false;
    }

}