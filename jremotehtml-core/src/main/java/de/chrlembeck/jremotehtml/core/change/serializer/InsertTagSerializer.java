package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.InsertTagChange;
import de.chrlembeck.jremotehtml.core.element.HTMLDomNode;
import de.chrlembeck.jremotehtml.core.element.HTMLElement;

public class InsertTagSerializer extends JsonSerializer<InsertTagChange> {

    @Override
    public void serialize(InsertTagChange change, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        HTMLDomNode newChild = change.getNewChild();
        HTMLElement parent = change.getParentTag();
        jgen.writeStartObject();
        jgen.writeStringField("action", "insertTag");
        jgen.writeNumberField("parentId", parent.getId());
        jgen.writeNumberField("position", change.getPosition());
        StringWriter writer = new StringWriter();
        newChild.render(writer);
        jgen.writeStringField("content", writer.toString());
        jgen.writeEndObject();
    }
}