package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.HTMLElement;

public class ValueListenerChange implements Change {

	private HTMLElement tag;

	private boolean enabled;

	public ValueListenerChange(HTMLElement tag, boolean enabled) {
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