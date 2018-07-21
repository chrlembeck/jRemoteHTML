package de.chrlembeck.javahtml;


public class AppendTag implements Change {

	private Tag parentTag;

	private HTMLElement newChild;

	public AppendTag(Tag parentTag, HTMLElement newChild) {
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