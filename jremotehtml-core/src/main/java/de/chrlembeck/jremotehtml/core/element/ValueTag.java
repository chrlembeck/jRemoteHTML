package de.chrlembeck.jremotehtml.core.element;

import java.util.List;

import de.chrlembeck.jremotehtml.core.change.Change;
import de.chrlembeck.jremotehtml.core.change.ValueListenerChange;

public class ValueTag extends HTMLElement {

	private static final long serialVersionUID = 6033472849725990826L;

	private boolean valueListenerEnabled;

	public ValueTag(String name) {
		super(name);
	}

	public void setValueListenerEnabled(boolean enabled) {
		notifyChange(new ValueListenerChange(this, enabled));
		this.valueListenerEnabled = enabled;
	}

	public boolean isValueListenerEnabled() {
		return valueListenerEnabled;
	}

	@Override
	public void collectListeners(List<Change> listeners) {
		super.collectListeners(listeners);
		if (valueListenerEnabled) {
			listeners.add(new ValueListenerChange(this, true));
		}
	}

	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String newValue) {
		setAttribute("value", newValue);
	}
}