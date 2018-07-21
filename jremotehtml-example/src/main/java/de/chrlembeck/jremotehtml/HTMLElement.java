package de.chrlembeck.jremotehtml;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface HTMLElement {

	void render(Page page, Writer writer) throws IOException;

	void collectListeners(List<Change> listeners);

}
