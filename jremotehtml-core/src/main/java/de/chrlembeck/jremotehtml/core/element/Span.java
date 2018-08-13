package de.chrlembeck.jremotehtml.core.element;

public class Span extends ContainerTag {

    private static final long serialVersionUID = 2050048533544808209L;

    public Span(String text) {
        super("span");
        appendElement(new TextNode(text));
    }
}