package de.chrlembeck.jremotehtml.core;

import de.chrlembeck.jremotehtml.core.element.Tag;

@FunctionalInterface
public interface ClickListener {

	public void tagClicked(Tag tag);
}