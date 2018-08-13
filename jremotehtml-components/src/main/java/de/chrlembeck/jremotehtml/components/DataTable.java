package de.chrlembeck.jremotehtml.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.chrlembeck.jremotehtml.core.element.GenericTag;
import de.chrlembeck.jremotehtml.core.element.HTMLElement;

public class DataTable<T extends Serializable> extends HTMLElement implements Serializable {

	private static final long serialVersionUID = 6107467213937219702L;

	private final List<DataTableColumn<T, ?>> columns = new ArrayList<>();

	private final List<T> data = new ArrayList<>();

	private final GenericTag tBody = new GenericTag("tbody");

	private final GenericTag tHead = new GenericTag("thead");

	private final GenericTag tFoot = new GenericTag("tfoot");

	public DataTable() {
		super("table");
		appendElement(tHead);
		appendElement(tBody);
		appendElement(tFoot);
		setAttribute("componentId", "jremotehtml-datatable");
		addClass("jremotehtml-datatable");
	}

	public int getColumnCount() {
		return columns.size();
	}

	public void addColumn(DataTableColumn<T, ?> column) {
		addColumn(getColumnCount(), column);
	}

	public void addColumn(int index, DataTableColumn<T, ?> column) {
		columns.add(index, column);
		tHead.insertElement(index, column.getThElement());

		for (int i = 0; i < tBody.getChildCount(); i++) {
			GenericTag tr = (GenericTag) tBody.childAt(i);
			tr.insertElement(index, createDataCell(data.get(i), column));
		}
	}

	public void addRow(final T object) {
		data.add(object);
		GenericTag tr = createRow(object);
		tBody.appendElement(tr);
	}

	private GenericTag createRow(final T object) {
		GenericTag tr = new GenericTag("tr");
		tr.addClass("jremotehtml-datatable-row");
		tr.setAttribute("tabindex", "0");
		for (int i = 0; i < getColumnCount(); i++) {
			GenericTag td = createDataCell(object, columns.get(i));
			tr.appendElement(td);
		}
		return tr;
	}

	private GenericTag createDataCell(final T object, DataTableColumn<T, ?> column) {
		GenericTag td = new GenericTag("td");
		td.addClass("jremotehtml-datatable");
		td.appendTextElement(column.getCellValue(object).toString());
		return td;
	}
}