package de.chrlembeck.jremotehtml;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.chrlembeck.jremotehtml.components.DataTable;
import de.chrlembeck.jremotehtml.components.DataTableColumn;
import de.chrlembeck.jremotehtml.components.ModalMessage;
import de.chrlembeck.jremotehtml.components.layout.BorderLayout;
import de.chrlembeck.jremotehtml.components.layout.BorderLayout.Pos;
import de.chrlembeck.jremotehtml.core.ClickListener;
import de.chrlembeck.jremotehtml.core.PageRegistry;
import de.chrlembeck.jremotehtml.core.element.BodyTag;
import de.chrlembeck.jremotehtml.core.element.Button;
import de.chrlembeck.jremotehtml.core.element.GenericTag;
import de.chrlembeck.jremotehtml.core.element.HTMLDomNode;
import de.chrlembeck.jremotehtml.core.element.HTMLElement;
import de.chrlembeck.jremotehtml.core.element.Input;
import de.chrlembeck.jremotehtml.core.element.Input.Type;
import de.chrlembeck.jremotehtml.core.element.Page;
import de.chrlembeck.jremotehtml.core.element.Span;
import de.chrlembeck.jremotehtml.core.element.Table;
import de.chrlembeck.jremotehtml.core.element.TagFactory;
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
		page.registerComponent("jremotehtml-components");
		BodyTag body = page.getBodyNode();
		Span span = new Span("test");
		body.appendElement(span);
		body.appendElement(new Span("noch ein Test"));
		span.addClickListener(tag -> System.out.println(tag.getId()));
		span.addClickListener(tag -> body.insertElement(0, new TextNode("clicked")));
		final Table table = createTable();
		body.appendElement(table);
		body.appendElement(new Button("Hinzufügen", event -> insertRow(table)));
		body.appendElement(new Button("Löschen", event -> removeRow(table)));
		body.appendElement(new Button("Name löschen", event -> removeNameFromRow(table)));
		body.appendElement(new Button("Rahmen", event -> changeBorder(table)));

		TextNode text = new TextNode("text");
		body.appendElement(text);
		body.appendElement(new Button("text", event -> changeText(text)));

		Button btListener = new Button("Do something");

		body.appendElement(new Button("Toggle Listener", event -> toggleListener(btListener)));
		body.appendElement(btListener);
		body.appendElement(new Button("Message",
				event -> ModalMessage.showInfoMessage(page, "Hello", "Hello World!", "Schließen")));

		Span span2 = new Span("");
		span2.appendElement(new TextNode("Node1 "));
		span2.appendElement(new TextNode("Node2 "));
		body.appendElement(span2);

		BorderLayout borderLayout = new BorderLayout();
		borderLayout.addComponent(Pos.TOP, TagFactory.createSpan("top").create());
		borderLayout.addComponent(Pos.BOTTOM, TagFactory.createSpan("bottom").create());
		borderLayout.addComponent(Pos.LEFT, TagFactory.createSpan("left").create());
		borderLayout.addComponent(Pos.RIGHT, TagFactory.createSpan("right").create());
		borderLayout.addComponent(Pos.CENTER, TagFactory.createSpan("center").create());
		borderLayout.setStyleAttribute("width", "50%");

		Input nameInput = new Input(Type.TEXT);
		body.appendElement(nameInput);
		Span spName = new Span(" ");
		body.appendElement(spName);

		btListener.addClickListener(e -> {
			((TextNode) spName.childAt(0)).setText(nameInput.getValue());
		});

		body.appendElement(borderLayout);

		body.appendElement(createDataTable());
		return page;
	}

	private HTMLDomNode createDataTable() {
		DataTable<Person> table = new DataTable<>();
		DataTableColumn<Person, Integer> colId = new DataTableColumn<>("ID", Person::getId);
		DataTableColumn<Person, String> colFirstName = new DataTableColumn<>("Vorname", Person::getFirstname);
		DataTableColumn<Person, String> colLastName = new DataTableColumn<>("Nachname", Person::getLastname);
		DataTableColumn<Person, LocalDate> colBirthday = new DataTableColumn<>("Geburtstag", Person::getBirthday);

		table.addColumn(colId);
		table.addColumn(colLastName);
		table.addColumn(colFirstName);

		table.addRow(new Person(1, "Meier", "Wolfgang", LocalDate.of(1977, 3, 27)));
		table.addColumn(colBirthday);
		table.addRow(new Person(2, "Müller", "Luise", LocalDate.of(1980, 1, 1)));
		table.addRow(new Person(3, "Schmidt", "Dieter", LocalDate.of(2001, 12, 31)));
		return table;
	}

	private void toggleListener(Button button) {
		List<ClickListener> clickListeners = button.getClickListeners();
		if (clickListeners.size() == 0) {
			button.addClickListener(l -> System.err.println(l.getId()));
		} else {
			button.removeClickListener(clickListeners.get(0));
		}
	}

	private void changeText(TextNode text) {
		text.setText(LocalTime.now().toString());
	}

	private void changeBorder(Table table) {
		String border = table.getAttribute("border");
		if (border == null) {
			table.setAttribute("border", "1");
			table.setStyleAttribute("border-color", "red");
		} else if ("1".equals(border)) {
			table.setAttribute("border", "3");
			table.setStyleAttribute("border-color", "green");
		} else {
			table.removeAttribute("border");
			table.removeStyleAttribute("border-color");
		}
	}

	public Table createTable() {
		Table table = new Table();
		HTMLElement headerRow = createRow("th", "Name", "Vorname");
		table.getHeader().appendElement(headerRow);
		return table;
	}

	private HTMLElement createRow(String type, String... content) {
		GenericTag row = new GenericTag("tr");
		for (String value : content) {
			GenericTag data = new GenericTag(type);
			data.appendElement(new TextNode(value));
			row.appendElement(data);
		}
		return row;
	}

	public void insertRow(Table table) {
		GenericTag tbody = table.getBody();
		tbody.appendElement(createRow("td", "Hans " + tbody.getChildCount(), "Mustermann " + tbody.getChildCount()));
	}

	public void removeRow(Table table) {
		HTMLElement tbody = table.getBody();
		HTMLElement row = (HTMLElement) tbody.childAt(tbody.getChildCount() - 1);
		tbody.removeElement(row);
	}

	public void removeNameFromRow(Table table) {
		HTMLElement tbody = table.getBody();
		HTMLElement row = (HTMLElement) tbody.childAt(tbody.getChildCount() - 1);
		HTMLElement data = (HTMLElement) row.childAt(0);
		HTMLDomNode textNode = data.childAt(0);
		data.removeElement(textNode);
	}

	static class Person implements Serializable {

		private int id;

		private String lastname;

		private String firstname;

		private LocalDate birthday;

		public Person(int id, String lastname, String firstname, LocalDate birthday) {
			super();
			this.id = id;
			this.lastname = lastname;
			this.firstname = firstname;
			this.birthday = birthday;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getLastname() {
			return lastname;
		}

		public void setLastname(String lastname) {
			this.lastname = lastname;
		}

		public String getFirstname() {
			return firstname;
		}

		public void setFirstname(String firstname) {
			this.firstname = firstname;
		}

		public LocalDate getBirthday() {
			return birthday;
		}

		public void setBirthday(LocalDate birthday) {
			this.birthday = birthday;
		}
	}
}