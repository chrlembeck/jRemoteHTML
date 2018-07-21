package de.chrlembeck.jremotehtml.core;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface HTMLElement {

    void render(Page page, Writer writer) throws IOException;

    void collectListeners(List<Change> listeners);

    /**
     * Prüft, ob der Knoten dem Client schon bekannt ist, also bereits einmal an
     * den Client gesendet wurde.
     */
    boolean isNewNode();

    void unsetId();
}