package de.chrlembeck.jremotehtml.core.element;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import de.chrlembeck.jremotehtml.core.change.AppendTag;
import de.chrlembeck.jremotehtml.core.change.Change;
import de.chrlembeck.jremotehtml.core.change.NewClickListener;
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
        module.addSerializer(AppendTag.class, new AppendTagSerializer(this));
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
        findNewElements(result);
        findModifiedProperties(result);
        findModifiedListeners(result);
        return result;
    }

    private void findModifiedListeners(List<Change> result) {
        // TODO Auto-generated method stub

    }

    private void findModifiedProperties(List<Change> result) {
        // TODO Auto-generated method stub

    }

    private void findNewElements(List<Change> result) {
        // TODO Auto-generated method stub

    }

    private void findRemovedElements(List<Change> result) {
        // TODO Auto-generated method stub

    }

    public void sendListeners(HttpServletResponse resp) throws IOException {
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(NewClickListener.class, new NewClickListenerSerializer(this));
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

    public Tag getTagById(String elementId) {
        return bodyNode.getTagById(elementId);
    }
}