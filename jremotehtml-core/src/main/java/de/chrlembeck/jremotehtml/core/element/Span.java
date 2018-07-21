package de.chrlembeck.jremotehtml.core.element;

public class Span extends Tag {


	public Span(String text) {
		super("span");
		appendElement(new TextNode(text));
	}
}