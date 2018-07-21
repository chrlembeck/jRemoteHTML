package de.chrlembeck.javahtml;

public class Span extends Tag {


	public Span(String text) {
		super("span");
		appendElement(new TextNode(text));
	}
}