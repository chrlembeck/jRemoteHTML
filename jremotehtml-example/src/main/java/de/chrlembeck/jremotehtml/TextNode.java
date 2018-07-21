package de.chrlembeck.jremotehtml;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class TextNode implements HTMLElement {

	private String text;

	public TextNode(String text) {
		this.text = text;
	}

	@Override
	public void render(Page page, Writer writer) throws IOException {
		writer.append(text);
	}

	@Override
	public void collectListeners(List<Change> listeners) {
	}
}