package de.chrlembeck.jremotehtml;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.chrlembeck.jremotehtml.core.PageRegistry;
import de.chrlembeck.jremotehtml.core.element.HTMLElement;
import de.chrlembeck.jremotehtml.core.element.Page;
import de.chrlembeck.jremotehtml.core.element.Span;
import de.chrlembeck.jremotehtml.core.element.Table;
import de.chrlembeck.jremotehtml.core.element.Tag;
import de.chrlembeck.jremotehtml.core.element.TextNode;

@Configuration
public class PageConfiguration {

    @Bean
    public PageRegistry pageRegistry() {
        PageRegistry pageRegistry = new PageRegistry();
        pageRegistry.setDefaultPageName("default");
        pageRegistry.registerCreator("default", this::createDefaultPage);

        return pageRegistry;
    }

    public Page createDefaultPage(String name) {
        Page page = new Page();
        Tag body = page.getBodyNode();
        Span span = new Span("test");
        body.appendElement(span);
        body.appendElement(new Span("noch ein Test"));
        span.addClickListener(tag -> System.out.println(tag.getId()));
        span.addClickListener(tag -> body.insertElement(0, new TextNode("clicked")));
        final Table table = createTable();
        body.appendElement(table);
        Tag btAdd = new Tag("button");
        btAdd.appendElement(new TextNode("hinzufügen"));
        btAdd.addClickListener(event -> insertRow(table));
        body.appendElement(btAdd);
        Tag btRemove = new Tag("button");
        btRemove.appendElement(new TextNode("Löschen"));
        btRemove.addClickListener(event -> removeRow(table));
        body.appendElement(btRemove);
        Tag btRemoveName = new Tag("button");
        btRemoveName.appendElement(new TextNode("Name löschen"));
        btRemoveName.addClickListener(event -> removeNameFromRow(table));
        body.appendElement(btRemoveName);
        Tag btBorder = new Tag("button");
        btBorder.appendElement(new TextNode("Rahmen"));
        btBorder.addClickListener(event -> changeBorder(table));
        body.appendElement(btBorder);

        Tag span2 = new Tag("span");
        span2.appendElement(new TextNode("Node1 "));
        span2.appendElement(new TextNode("Node2 "));
        body.appendElement(span2);
        return page;
    }

    private void changeBorder(Table table) {
        String border = table.getAttribute("border");
        if (border == null) {
            table.setAttribute("border", "1");
        } else if ("1".equals(border)) {
            table.setAttribute("border", "3");
        } else {
            table.removeAttribute("border");
        }
    }

    public Table createTable() {
        Table table = new Table();
        Tag headerRow = createRow("th", "Name", "Vorname");
        table.getHeader().appendElement(headerRow);
        return table;
    }

    private Tag createRow(String type, String... content) {
        Tag row = new Tag("tr");
        for (String value : content) {
            Tag data = new Tag(type);
            data.appendElement(new TextNode(value));
            row.appendElement(data);
        }
        return row;
    }

    public void insertRow(Table table) {
        Tag tbody = table.getBody();
        tbody.appendElement(createRow("td", "Hans " + tbody.getChildCount(), "Mustermann " + tbody.getChildCount()));
    }

    public void removeRow(Table table) {
        Tag tbody = table.getBody();
        Tag row = (Tag) tbody.childAt(tbody.getChildCount() - 1);
        tbody.removeElement(row);
    }

    public void removeNameFromRow(Table table) {
        Tag tbody = table.getBody();
        Tag row = (Tag) tbody.childAt(tbody.getChildCount() - 1);
        Tag data = (Tag) row.childAt(0);
        HTMLElement textNode = data.childAt(0);
        data.removeElement(textNode);
    }
}