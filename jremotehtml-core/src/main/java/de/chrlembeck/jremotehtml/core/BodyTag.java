package de.chrlembeck.jremotehtml.core;

public class BodyTag extends Tag {

	public BodyTag() {
		super("body");
		setAttribute("onload", "loadContent()");
	}
}