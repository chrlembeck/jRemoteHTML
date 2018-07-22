package de.chrlembeck.jremotehtml.core.element;

import de.chrlembeck.jremotehtml.core.ClickListener;

public class Button extends Tag {

    private static final long serialVersionUID = 3896865565418897662L;

    public Button() {
        super("button");
    }

    public Button(String text) {
        this();
        appendTextElement(text);
    }

    public Button(String text, ClickListener listener) {
        this(text);
        addClickListener(listener);
    }
}