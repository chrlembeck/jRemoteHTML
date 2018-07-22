package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import de.chrlembeck.jremotehtml.core.change.Change;

public interface HTMLElement extends Serializable {

    void render(Writer writer) throws IOException;

    void collectListeners(List<Change> listeners);

    /**
     * Prüft, ob der Knoten dem Client schon bekannt ist, also bereits einmal an
     * den Client gesendet wurde.
     */
    boolean isNewNode();

    void unsetIds();

    void setParent(Tag tag);
}