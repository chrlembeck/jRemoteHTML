package de.chrlembeck.jremotehtml.core.change;

import de.chrlembeck.jremotehtml.core.element.HTMLElement;
import de.chrlembeck.jremotehtml.core.element.Tag;

public class AppendTagChange implements Change {

	private Tag parentTag;

	private HTMLElement newChild;

	public AppendTagChange(Tag parentTag, HTMLElement newChild) {
		this.parentTag = parentTag;
		this.newChild = newChild;
	}

	public Tag getParentTag() {
		return parentTag;
	}

	public HTMLElement getNewChild() {
		return newChild;
	}

}