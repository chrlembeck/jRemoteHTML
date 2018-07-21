package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.Tag;

public class NewClickListenerChange implements Change {

	private Tag tag;

	public NewClickListenerChange(Tag tag) {
		this.tag = tag;
	}

	public Tag getTag() {
		return tag;
	}
}
