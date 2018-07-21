package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.Tag;

public class NewClickListener implements Change {

	private Tag tag;

	public NewClickListener(Tag tag) {
		this.tag = tag;
	}

	public Tag getTag() {
		return tag;
	}
}
