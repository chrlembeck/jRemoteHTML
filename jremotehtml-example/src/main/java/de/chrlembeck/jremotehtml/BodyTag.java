package de.chrlembeck.jremotehtml;

public class BodyTag extends Tag {

	public BodyTag() {
		super("body");
		setAttribute("onload", "loadContent()");
	}
}