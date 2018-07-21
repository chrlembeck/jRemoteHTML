package de.chrlembeck.javahtml;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public abstract class Tag implements HTMLElement {

	private String id;

	private String name;

	private Tag parent;

	private List<ClickListener> clickListeners = new ArrayList<>();

	private PageChangeListener changeListener;

	private List<HTMLElement> children = new LinkedList<>();

	private Map<String, String> attributes = new TreeMap<>();

	@Override
	public final void render(Page page, Writer writer) throws IOException {
		writer.write("<" + name + " id=\"" + getId(page) + "\"");
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

	public void setChangeListener(PageChangeListener changeListener) {
		this.changeListener = changeListener;
	}

	public Tag(String name) {
		this.name = name;
	}

	public String getId(Page page) {
		if (id == null) {
			id = "i" + page.nextId();
		}
		return id;
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
		notifyChange(new AppendTag(this, tag));
	}

	private void notifyChange(Change change) {
		if (changeListener != null) {
			changeListener.changeHappened(change);
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
			for (HTMLElement element:children) {
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
}