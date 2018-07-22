package de.chrlembeck.jremotehtml.core.element;

public class Table extends Tag {

    private static final long serialVersionUID = -2957195969417418887L;

    private Tag body;

    private Tag header;

    private Tag footer;

    public Table() {
        super("table");
        header = new Tag("thead");
        body = new Tag("tbody");
        footer = new Tag("tfoot");
        appendElement(header);
        appendElement(body);
        appendElement(footer);
    }

    public Tag getBody() {
        return body;
    }

    public Tag getFooter() {
        return footer;
    }

    public Tag getHeader() {
        return header;
    }
}