package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import de.chrlembeck.jremotehtml.core.change.Change;

public interface HTMLDomNode extends Serializable {

    void render(Writer writer) throws IOException;

    void collectListeners(List<Change> listenerChanges);

    void collectStyles(List<Change> styleChanges);

    /**
     * Prüft, ob der Knoten dem Client schon bekannt ist, also bereits einmal an
     * den Client gesendet wurde.
     */
    boolean isNewNode();

    void setParent(HTMLElement tag);
}