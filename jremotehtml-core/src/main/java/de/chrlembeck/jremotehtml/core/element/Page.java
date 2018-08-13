package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.chrlembeck.jremotehtml.core.change.AttributeModifiedChange;
import de.chrlembeck.jremotehtml.core.change.AttributeRemovedChange;
import de.chrlembeck.jremotehtml.core.change.Change;
import de.chrlembeck.jremotehtml.core.change.ClickListenerChange;
import de.chrlembeck.jremotehtml.core.change.InsertTagChange;
import de.chrlembeck.jremotehtml.core.change.RemoveElementChange;
import de.chrlembeck.jremotehtml.core.change.StyleModifiedChange;
import de.chrlembeck.jremotehtml.core.change.StyleRemovedChange;
import de.chrlembeck.jremotehtml.core.change.TextModifiedChange;
import de.chrlembeck.jremotehtml.core.change.ValueListenerChange;
import de.chrlembeck.jremotehtml.core.change.serializer.AttributeModifiedSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.AttributeRemovedSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.ClickListenerSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.InsertTagSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.RemoveElementSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.StyleModifiedSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.StyleRemovedSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.TextModifiedSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.ValueListenerSerializer;
import de.chrlembeck.jremotehtml.core.util.LoggingWriter;

public class Page implements Serializable {

    private static final long serialVersionUID = 2050048533544808209L;

    private static Logger LOGGER = LoggerFactory.getLogger(Page.class);

    public static final String ROOT_NODE_ID = "root";

    /**
     * Id für das nächste an den Client zu sendende Element.
     */
    private int id;

    /**
     * Zuletzt an den Client gesandte ElementId;
     */
    private int lastSentId;

    private final BodyTag bodyNode;

    private List<Change> changes = new ArrayList<>();

    private Set<TextNode> newTextNodes = new HashSet<>();

    private Set<TextNode> modifiedTextNodes = new HashSet<>();

	private List<String> components = new ArrayList<>();

    public Page() {
        bodyNode = new BodyTag(this);
        bodyNode.setId(nextId());
        clearChanges();
    }

	public void registerComponent(String name) {
		components.add(name);
	}

    public void render(PrintWriter writer) throws IOException {
        writer.write("<!DOCTYPE html>\n");
        writer.write("<html>\n");
        writer.write("<head>");
		writer.write("<link rel=\"stylesheet\" href=\"../jremotehtml.css\"/>");
		for (String component : components) {
			writer.write("<link rel=\"stylesheet\" href=\"../" + component + ".css\"/>");
		}

		writer.write("<script type=\"text/javascript\" src=\"../jremotehtml.js\"></script>");
		for (String component : components) {
			writer.write("<script type=\"text/javascript\" src=\"../" + component + ".js\"></script>");
		}
        writer.write("</head>\n");
		writer.write("<body id=\"" + bodyNode.getId() + "\" onload=\"jremotehtml.loadContent()\"></body>");
        writer.write("</html>");
    }

    public int nextId() {
        return id++;
    }

    public int getLastSentId() {
        return lastSentId;
    }

    public void sendChanges(HttpServletResponse resp) throws IOException {
        List<Change> changesToSend = collectChanges();

        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        resp.setCharacterEncoding("UTF-8");
		ObjectMapper objectMapper = createObjectMapper();

        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8");
                LoggingWriter logger = new LoggingWriter(writer)) {
            objectMapper.writeValue(logger, changesToSend);
            LOGGER.debug("Sending: " + logger.toString());
        }
        lastSentId = id - 1;
        clearChanges();
    }

	private ObjectMapper createObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(InsertTagChange.class, new InsertTagSerializer());
        module.addSerializer(ClickListenerChange.class, new ClickListenerSerializer());
		module.addSerializer(ValueListenerChange.class, new ValueListenerSerializer());
        module.addSerializer(RemoveElementChange.class, new RemoveElementSerializer());
        module.addSerializer(AttributeModifiedChange.class, new AttributeModifiedSerializer());
        module.addSerializer(AttributeRemovedChange.class, new AttributeRemovedSerializer());
        module.addSerializer(StyleModifiedChange.class, new StyleModifiedSerializer());
        module.addSerializer(StyleRemovedChange.class, new StyleRemovedSerializer());
        module.addSerializer(TextModifiedChange.class, new TextModifiedSerializer());
        objectMapper.registerModule(module);
		return objectMapper;
	}

    private List<Change> collectChanges() {
        List<Change> result = new ArrayList<>();
        findRemovedElements(result);
        findModifiedAttributes(result);
        findModifiedListeners(result);
        findNewElements(result);
        findTextModifications(result);
        return result;
    }

    private void findRemovedElements(List<Change> result) {
        changes.stream().filter(change -> change instanceof RemoveElementChange).forEach(result::add);
    }

    private void findModifiedAttributes(List<Change> result) {
        changes.stream()
                .filter(change -> change instanceof AttributeModifiedChange || change instanceof AttributeRemovedChange
                        || change instanceof StyleModifiedChange || change instanceof StyleRemovedChange)
                .forEach(result::add);
    }

    private void findModifiedListeners(List<Change> result) {
        changes.stream().filter(ClickListenerChange.class::isInstance).forEach(result::add);
    }

    private void findNewElements(List<Change> changes) {
        Queue<HTMLElement> queue = new LinkedList<>();
        Assert.isTrue(bodyNode.isNewNode() == false, "Der BodyNode darf nie 'neu' sein.");
        queue.add(bodyNode);
        while (!queue.isEmpty()) {
            HTMLElement currentTag = queue.poll();
            for (int index = 0; index < currentTag.getChildCount(); index++) {
                final HTMLDomNode element = currentTag.childAt(index);
                if (element.isNewNode()) {
                    if (element instanceof HTMLElement) {
                        // Bei neuen Tags jetzt erst einmal neue Ids vergeben
                        ((HTMLElement) element).assignIds(this);
                    } else {
                        newTextNodes.remove(element);
                    }
                    // den neuen Knoten in die Liste der Client-Änderungen
                    // übernehmen
                    changes.add(new InsertTagChange(currentTag, element, index));
                    // die Listener für die neuen Knoten hinzufügen
                    element.collectListeners(changes);
                    element.collectStyles(changes);
                } else {
                    if (element instanceof HTMLElement) {
                        // TextNodes müssen nicht weiter überprüft werden, da
                        // sie keine children haben können.
                        queue.offer((HTMLElement) element);
                    }
                }
            }
        }
    }

    private void findTextModifications(List<Change> result) {
        for (TextNode node : modifiedTextNodes) {
            if (!node.isNewNode() && node.getParent() != null) {
                int position = 0;
                HTMLElement parent = node.getParent();
                boolean found = false;
                for (int index = 0; index < parent.getChildCount(); index++) {
                    HTMLDomNode child = parent.childAt(index);
                    if (child == node) {
                        found = true;
                        break;
                    }
                    if (!child.isNewNode()) {
                        position++;
                    }
                }
                Assert.isTrue(found, "Der Textknoten wurde nicht gefunden.");
                result.add(new TextModifiedChange(parent.getId(), position, node.getText()));
            }
        }
    }

    public void changeHappened(Change change) {
        changes.add(change);
    }

    public BodyTag getBodyNode() {
        return bodyNode;
    }

    public void clearChanges() {
        changes.clear();
        newTextNodes.clear();
        modifiedTextNodes.clear();
    }

    public HTMLElement getTagById(int elementId) {
        return bodyNode.getElementById(elementId);
    }

    public void registerNewTextNode(TextNode element) {
        newTextNodes.add(element);
    }

    public void registerModifiedTextNode(TextNode element) {
        modifiedTextNodes.add(element);
    }

    /**
     * Should only be used by {@link TextNode#isNewNode()}, since
     * {@link #newTextNodes} only contains the new TextNodes, that are direct
     * children of already known nodes to the client.
     */
    boolean isNewTextNode(TextNode node) {
        return newTextNodes.contains(node);
    }

    @Override
    public String toString() {
        return bodyNode.toString();
    }
}