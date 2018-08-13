package de.chrlembeck.jremotehtml.components;

import java.io.Serializable;
import java.util.function.Function;

import de.chrlembeck.jremotehtml.core.element.GenericTag;
import de.chrlembeck.jremotehtml.core.element.HTMLDomNode;
import de.chrlembeck.jremotehtml.core.element.TextNode;

public class DataTableColumn<T, V> implements Serializable {

	private static final long serialVersionUID = -5831777959200434434L;

	private GenericTag thElement;

	private TextNode headerText;

	private Function<T, V> cellValueExtractor;

	public DataTableColumn(String header, Function<T, V> cellValueExtractor) {
		thElement = new GenericTag("th");
		thElement.addClass("jremotehtml-datatable");
		headerText = new TextNode(header);
		thElement.appendElement(headerText);
		this.cellValueExtractor = cellValueExtractor;
	}

	public void setHeader(String newName) {
		headerText.setText(newName);
	}

	public String getHeader() {
		return headerText.getText();
	}

	public HTMLDomNode getThElement() {
		return thElement;
	}

	public V getCellValue(T object) {
		return cellValueExtractor.apply(object);
	}
}