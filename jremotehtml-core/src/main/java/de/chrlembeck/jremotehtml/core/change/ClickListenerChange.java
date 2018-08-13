package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.HTMLElement;

public class ClickListenerChange implements Change {

    private HTMLElement tag;

    private boolean enabled;

    public ClickListenerChange(HTMLElement tag, boolean enabled) {
        this.tag = tag;
        this.enabled = enabled;
    }

    public HTMLElement getTag() {
        return tag;
    }

    public boolean isEnabled() {
        return enabled;
    }
}