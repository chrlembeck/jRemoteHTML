package de.chrlembeck.jremotehtml.core;

import de.chrlembeck.jremotehtml.core.element.HTMLElement;

@FunctionalInterface
public interface ClickListener {

	public void tagClicked(HTMLElement tag);
}