package de.chrlembeck.jremotehtml.core;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.Assert;

public abstract class Tag implements HTMLElement {

    public static final int NO_ID = -1;

    private int id = NO_ID;

    private String name;

    private Tag parent;

    private List<ClickListener> clickListeners = new ArrayList<>();

    private List<HTMLElement> children = new LinkedList<>();

    private Map<String, String> attributes = new TreeMap<>();

    @Override
    public final void render(Page page, Writer writer) throws IOException {
        Assert.isTrue(id != NO_ID, "Zu diesem Zeitpunkt sollte der Knoten eine ID besitzen.");
        writer.write("<" + name + " id=\"" + getId() + "\"");
        for (Map.Entry<String, String> attribute : attributes.entrySet()) {
            writer.write(" ");
            writer.write(attribute.getKey());
            writer.write("=\"");
            writer.write(attribute.getValue());
            writer.write("\"");
        }
        writer.write(">");
        for (HTMLElement element : children) {
            element.render(page, writer);
        }
        writer.write("</" + name + ">\n");
    }

    public Tag(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public Tag getParent() {
        return parent;
    }

    public void addClickListener(ClickListener listener) {
        clickListeners.add(listener);
        notifyChange(new AddClickListenerChange(this));
    }

    public void appendElement(HTMLElement tag) {
        children.add(tag);
    }

    protected void notifyChange(Change change) {
        if (parent != null) {
            parent.notifyChange(change);
        }
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    @Override
    public void collectListeners(List<Change> listeners) {
        if (clickListeners != null && !clickListeners.isEmpty()) {
            listeners.add(new NewClickListener(this));
        }
        for (HTMLElement element : children) {
            element.collectListeners(listeners);
        }
    }

    public void fireElementClicked() {
        for (ClickListener listener : clickListeners) {
            listener.tagClicked(this);
        }
    }

    public Tag getTagById(String elementId) {
        if (elementId.equals(this.id)) {
            return this;
        } else {
            for (HTMLElement element : children) {
                if (element instanceof Tag) {
                    Tag tag = (Tag) element;
                    Tag result = tag.getTagById(elementId);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    public void removeElement(HTMLElement element) {
        // TODO Löschung in der Page eintragen (falls erreichbar)
        // Bei Tags die ID des Tags merken, bei TextNodes die Position des Nodes
        // (die auf dem Client haben sollte). Dafür müssen die Geschwister vor
        // dem Textknoten gezählt werden, die nicht neu sind.

        element.unsetId();
    }

    @Override
    public void unsetId() {
        id = NO_ID;
    }

    public void assignIds(Page page) {
        if (id == NO_ID) {
            id = page.nextId();
        }
        children.stream().filter(e -> (e instanceof Tag)).map(e -> (Tag) e).forEach(e -> e.assignIds(page));
    }

    @Override
    public boolean isNewNode() {
        if (parent == null) {
            return true;
        }
        final Page page = getPage();
        return page == null || page.getLastSentId() >= id;
    }

    protected Page getPage() {
        return parent == null ? null : parent.getPage();
    }
}