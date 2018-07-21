package de.chrlembeck.jremotehtml.core;

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


public class Page implements PageChangeListener {

    private static Logger LOGGER = LoggerFactory.getLogger(Page.class);


    public static final String ROOT_NODE_ID = "root";

    private int id = 0;

    private Tag bodyNode = new BodyTag();

    private List<Change> changes = new ArrayList<>();

    public void render(PrintWriter writer) throws IOException {
        writer.write("<!DOCTYPE html>\n");
        writer.write("<html>\n");
        writer.write("<head>");
        writer.write("<script type=\"text/javascript\" src=\"../javahtml.js\">");
        writer.write("</script>");
        writer.write("</head>\n");
        bodyNode.render(this, writer);
        writer.write("</html>");
    }


    public int nextId() {
        return id++;
    }

    public void sendChanges(HttpServletResponse resp) throws IOException {
        resp.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        resp.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(AppendTag.class, new AppendTagSerializer(this));
        objectMapper.registerModule(module);

        try (OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), "UTF-8")) {
            objectMapper.writeValue(writer, changes);
        }
        clearChanges();
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

    @Override
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