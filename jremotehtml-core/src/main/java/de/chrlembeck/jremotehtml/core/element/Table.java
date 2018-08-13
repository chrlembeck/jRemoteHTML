package de.chrlembeck.jremotehtml.core.element;

public class Table extends HTMLElement {

    private static final long serialVersionUID = -2957195969417418887L;

	private GenericTag body;

	private GenericTag header;

	private GenericTag footer;

    public Table() {
        super("table");
		header = new GenericTag("thead");
		body = new GenericTag("tbody");
		footer = new GenericTag("tfoot");
        appendElement(header);
        appendElement(body);
        appendElement(footer);
    }

	public GenericTag getBody() {
        return body;
    }

	public GenericTag getFooter() {
        return footer;
    }

	public GenericTag getHeader() {
        return header;
    }
}