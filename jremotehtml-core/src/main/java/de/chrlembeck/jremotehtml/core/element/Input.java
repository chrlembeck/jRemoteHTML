package de.chrlembeck.jremotehtml.core.element;

import java.util.Locale;

public class Input extends ValueTag {

	private static final long serialVersionUID = 2041446583781374905L;

	public enum Type {
		TEXT, PASSWORD, RADIO, CHECKBOX
	}

	public Input(Type type) {
		super("input");
		setValueListenerEnabled(true);
		setAttribute("type", type.name().toLowerCase(Locale.US));
	}
}