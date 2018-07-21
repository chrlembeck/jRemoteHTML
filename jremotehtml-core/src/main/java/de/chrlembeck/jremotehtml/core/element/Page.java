package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.chrlembeck.jremotehtml.core.change.AppendTagChange;
import de.chrlembeck.jremotehtml.core.change.Change;
import de.chrlembeck.jremotehtml.core.change.NewClickListenerChange;
import de.chrlembeck.jremotehtml.core.change.serializer.AppendTagSerializer;
import de.chrlembeck.jremotehtml.core.change.serializer.NewClickListenerSerializer;

public class Page {

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

    private final BodyTag bodyNode = new BodyTag(this);

    private List<Change> changes = new ArrayList<>();

    public Page() {
        bodyNode.setId(nextId());
    }

    public void render(PrintWriter writer) throws IOException {
        writer.write("<!DOCTYPE html>\n");
        writer.write("<html>\n");
        writer.write("<head>");
        writer.write("<script type=\"text/javascript\" src=\"../javahtml.js\">");
        writer.write("</script>");
        writer.write("</head>\n");
        writer.write("<body id=\"" + bodyNode.getId() + "\" onload=\"loadContent()\"/>");
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
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(AppendTagChange.class, new AppendTagSerializer(this));
        module.addSerializer(NewClickListenerChange.class, new NewClickListenerSerializer());
        objectMapper.registerModule(module);

        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8")) {
            objectMapper.writeValue(writer, changesToSend);
        }
        lastSentId = id - 1;
        clearChanges();
    }

    private List<Change> collectChanges() {
        List<Change> result = new ArrayList<>();
        findRemovedElements(result);
        findModifiedProperties(result);
        findModifiedListeners(result);
        findNewElements(result);
        return result;
    }

    private void findRemovedElements(List<Change> result) {
        // TODO Auto-generated method stub

    }

    private void findModifiedProperties(List<Change> result) {
        // TODO Auto-generated method stub

    }

    private void findModifiedListeners(List<Change> result) {
        // TODO Auto-generated method stub

    }

    private void findNewElements(List<Change> changes) {
        Queue<Tag> queue = new LinkedList<>();
        Assert.isTrue(bodyNode.isNewNode() == false, "Der BodyNode darf nie 'neu' sein.");
        queue.add(bodyNode);
        while (!queue.isEmpty()) {
            Tag currentTag = queue.poll();
            for (HTMLElement element : currentTag) {
                if (element.isNewNode()) {
                    if (element instanceof Tag) {
                        // Bei neuen Tags jetzt erst einmal neue Ids vergeben
                        ((Tag) element).assignIds(this);
                    }
                    // den neuen Knoten in die Liste der Client-Änderungen
                    // übernehmen
                    changes.add(new AppendTagChange(currentTag, element));
                    // die Listener für die neuen Knoten hinzufügen
                    element.collectListeners(changes);
                } else if (element instanceof Tag) {
                    // TextNodes müssen nicht weiter überprüft werden, da sie
                    // keine children haben können.
                    queue.offer((Tag) element);
                }
            }
        }
    }

    public void sendListeners(HttpServletResponse resp) throws IOException {
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(NewClickListenerChange.class, new NewClickListenerSerializer());
        objectMapper.registerModule(module);
        List<Change> listeners = new ArrayList<>();
        getBodyNode().collectListeners(listeners);

        LOGGER.debug("register listeners: " + listeners);

        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8")) {
            objectMapper.writeValue(writer, listeners);
        }
    }

    public void changeHappened(Change change) {
        changes.add(change);
    }

    public Tag getBodyNode() {
        return bodyNode;
    }

    public void clearChanges() {
        changes.clear();
    }

    public Tag getTagById(int elementId) {
        return bodyNode.getTagById(elementId);
    }
}