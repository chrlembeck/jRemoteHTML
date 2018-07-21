package de.chrlembeck.jremotehtml.core;

public class BodyTag extends Tag {

    private Page page;

    public BodyTag() {
        super("body");
        setAttribute("onload", "loadContent()");
    }

    @Override
    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
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