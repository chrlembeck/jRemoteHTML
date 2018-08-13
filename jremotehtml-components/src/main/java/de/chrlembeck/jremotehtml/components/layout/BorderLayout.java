package de.chrlembeck.jremotehtml.components.layout;

import de.chrlembeck.jremotehtml.core.element.HTMLElement;

public class BorderLayout extends HTMLElement {

	public enum Pos {
		TOP("jremotehtml-borderlayout-top"), LEFT("jremotehtml-borderlayout-left"), CENTER(
				"jremotehtml-borderlayout-center"), RIGHT(
						"jremotehtml-borderlayout-right"), BOTTOM("jremotehtml-borderlayout-bottom");

		private final String cssClass;

		private Pos(String cssClass) {
			this.cssClass = cssClass;
		}

		public String getCssClass() {
			return cssClass;
		}
	}

	private static final long serialVersionUID = -2660178751175030377L;

	public BorderLayout() {
		super("div");
		addClass("jremotehtml-border-layout");
	}

	public void addComponent(Pos position, HTMLElement component) {
		component.addClass(position.getCssClass());
		appendElement(component);
	}


}