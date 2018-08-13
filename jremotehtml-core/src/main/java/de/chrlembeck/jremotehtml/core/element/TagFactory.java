package de.chrlembeck.jremotehtml.core.element;

import de.chrlembeck.jremotehtml.core.ClickListener;

public class TagFactory {

	public static class TagCreator<T extends HTMLElement> {

		private T tag;

		private TagCreator(T tag) {
			this.tag = tag;
		}

		public T create() {
			return tag;
		}

		public TagCreator<T> setClass(String className) {
			return setAttribute("class", className);
		}

		public TagCreator<T> setStyle(String style) {
			return setAttribute("style", style);
		}

		public TagCreator<T> setAttribute(String attributeName, String attributeValue) {
			tag.setAttribute(attributeName, attributeValue);
			return this;
		}

		public TagCreator<T> appendTextElement(String text) {
			tag.appendTextElement(text);
			return this;
		}

		public TagCreator<T> appendElement(HTMLElement script) {
			tag.appendElement(script);
			return this;
		}

		public TagCreator<T> addClickListener(ClickListener listener) {
			tag.addClickListener(listener);
			return this;
		}
	}

	public static TagCreator<GenericTag> createTag(String name) {
		return new TagCreator<GenericTag>(new GenericTag(name));
	}

	public static TagCreator<Span> createSpan(String content) {
		return new TagCreator<Span>(new Span(content));
	}

	public static TagCreator<GenericTag> createH2(String text) {
		GenericTag h2 = new GenericTag("H2");
		h2.appendTextElement(text);
		return new TagCreator<GenericTag>(h2);
	}

	public static TagCreator<Div> createDiv() {
		return new TagCreator<Div>(new Div());
	}

	public static TagCreator<Button> createButton(String caption) {
		return new TagCreator<Button>(new Button(caption));
	}

	public static TagCreator<Button> createButton(String caption, ClickListener listener) {
		return new TagCreator<Button>(new Button(caption, listener));
	}
}