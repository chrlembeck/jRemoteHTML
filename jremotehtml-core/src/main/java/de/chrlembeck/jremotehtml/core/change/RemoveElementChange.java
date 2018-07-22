package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.Tag;

public class RemoveElementChange implements Change {

    private final Integer id;

    private final int position;

    private final Tag parent;

    public RemoveElementChange(Tag parent, int position, int id) {
        this.parent = parent;
        this.position = position;
        this.id = id;
    }

    public RemoveElementChange(Tag parent, int position) {
        this.parent = parent;
        this.position = position;
        this.id = null;
    }

    public Tag getParent() {
        return parent;
    }

    public int getPosition() {
        return position;
    }

    public Integer getId() {
        return id;
    }
}