package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.Tag;

public class ClickListenerChange implements Change {

    private Tag tag;

    private boolean enabled;

    public ClickListenerChange(Tag tag, boolean enabled) {
        this.tag = tag;
        this.enabled = enabled;
    }

    public Tag getTag() {
        return tag;
    }

    public boolean isEnabled() {
        return enabled;
    }
}