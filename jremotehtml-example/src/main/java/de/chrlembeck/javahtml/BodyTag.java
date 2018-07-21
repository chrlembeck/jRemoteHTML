package de.chrlembeck.javahtml;

public class BodyTag extends Tag {

	public BodyTag() {
		super("body");
		setAttribute("onload", "loadContent()");
	}
}