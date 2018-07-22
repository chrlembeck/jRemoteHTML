package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.util.Assert;

import de.chrlembeck.jremotehtml.core.ClickListener;
import de.chrlembeck.jremotehtml.core.change.AttributeModifiedChange;
import de.chrlembeck.jremotehtml.core.change.AttributeRemovedChange;
import de.chrlembeck.jremotehtml.core.change.Change;
import de.chrlembeck.jremotehtml.core.change.ClickListenerChange;
import de.chrlembeck.jremotehtml.core.change.RemoveElementChange;
import de.chrlembeck.jremotehtml.core.change.StyleModifiedChange;
import de.chrlembeck.jremotehtml.core.change.StyleRemovedChange;

public class Tag implements HTMLElement, Iterable<HTMLElement> {

    private static final long serialVersionUID = 4057061365461142402L;

    public static final int NO_ID = -1;

    private int id = NO_ID;

    private String name;

    private Tag parent;

    private List<ClickListener> clickListeners = new ArrayList<>();

    private List<HTMLElement> children = new LinkedList<>();

    private Map<String, String> attributes = new TreeMap<>();

    private Map<String, String> styles = new TreeMap<>();

    @Override
    public final void render(Writer writer) throws IOException {
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
        boolean wasTextNode = false;
        for (HTMLElement element : children) {
            if (element instanceof TextNode) {
                if (wasTextNode) {
                    writer.append(TextNode.SEPARATOR);
                }
                element.render(writer);
                wasTextNode = true;
            } else {
                element.render(writer);
                wasTextNode = false;
            }
        }
        writer.write("</" + name + ">");
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

    @Override
    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public Tag getParent() {
        return parent;
    }

    public void addClickListener(ClickListener listener) {
        clickListeners.add(listener);
        if (clickListeners.size() == 1 && !isNewNode()) {
            notifyChange(new ClickListenerChange(this, true));
        }
    }

    public void removeClickListener(ClickListener clickListener) {
        clickListeners.remove(clickListener);
        if (clickListeners.isEmpty() && !isNewNode()) {
            notifyChange(new ClickListenerChange(this, false));
        }
    }

    public List<ClickListener> getClickListeners() {
        return clickListeners;
    }

    public void appendElement(HTMLElement element) {
        insertElement(getChildCount(), element);
    }

    public void appendTextElement(String text) {
        appendElement(new TextNode(text));
    }

    public void insertElement(int index, HTMLElement element) {
        children.add(index, element);
        element.setParent(this);
        if (!isNewNode() && element instanceof TextNode) {
            getPage().registerNewTextNode((TextNode) element);
        }
    }

    protected void notifyChange(Change change) {
        if (parent != null) {
            parent.notifyChange(change);
        }
    }

    @Override
    public void collectListeners(List<Change> listeners) {
        if (clickListeners != null && !clickListeners.isEmpty()) {
            listeners.add(new ClickListenerChange(this, true));
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

    public Tag getTagById(int elementId) {
        if (elementId == this.id) {
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

    public final void removeElement(HTMLElement element) {
        // Löschung in der Page eintragen (falls erreichbar)
        // Bei Tags die ID des Tags merken, bei TextNodes die Position des Nodes
        // (die auf dem Client haben sollte). Dafür müssen die Geschwister vor
        // dem Textknoten gezählt werden, die nicht neu sind.

        if (!isNewNode()) {
            int clientPosition = 0;
            for (int index = 0; index < getChildCount(); index++) {
                HTMLElement child = childAt(index);
                if (child == element) {
                    children.remove(index);
                    break;
                }
                if (!child.isNewNode()) {
                    clientPosition++;
                }
            }
            if (element instanceof Tag) {
                getPage().changeHappened(new RemoveElementChange(this, clientPosition, ((Tag) element).getId()));
            } else {
                getPage().changeHappened(new RemoveElementChange(this, clientPosition));
            }
        }
        element.unsetIds();
        element.setParent(null);
    }

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
        if (!isNewNode()) {
            getPage().changeHappened(new AttributeModifiedChange(getId(), key, value));
        }
    }

    public void setStyleAttribute(String key, String value) {
        styles.put(key, value);
        if (!isNewNode()) {
            getPage().changeHappened(new StyleModifiedChange(getId(), key, value));
        }
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
        if (!isNewNode()) {
            getPage().changeHappened(new AttributeRemovedChange(getId(), key));
        }
    }

    public void removeStyleAttribute(String key) {
        styles.remove(key);
        if (!isNewNode()) {
            getPage().changeHappened(new StyleRemovedChange(getId(), key));
        }
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public String getStyleAttribute(String key) {
        return styles.get(key);
    }

    @Override
    public void unsetIds() {
        id = NO_ID;
        children.stream().filter(element -> (element instanceof Tag)).map(element -> (Tag) element)
                .forEach(tag -> tag.unsetIds());
    }

    public void assignIds(final Page page) {
        Assert.isTrue(id == NO_ID, "Der Knoten darf vorher noch keine Id gehabt haben.");
        id = page.nextId();
        children.stream().filter(element -> (element instanceof Tag)).map(element -> (Tag) element)
                .forEach(tag -> tag.assignIds(page));
    }

    @Override
    public boolean isNewNode() {
        if (parent == null) {
            return true;
        }
        final Page page = getPage();
        return page == null || id == NO_ID || page.getLastSentId() < id;
    }

    protected Page getPage() {
        return parent == null ? null : parent.getPage();
    }

    @Override
    public Iterator<HTMLElement> iterator() {
        return children.iterator();
    }

    public int getChildCount() {
        return children.size();
    }

    public HTMLElement childAt(int index) {
        return children.get(index);
    }
}