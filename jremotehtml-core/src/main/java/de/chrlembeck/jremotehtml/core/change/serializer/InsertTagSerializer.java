package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.InsertTagChange;
import de.chrlembeck.jremotehtml.core.element.HTMLElement;
import de.chrlembeck.jremotehtml.core.element.Page;
import de.chrlembeck.jremotehtml.core.element.Tag;

public class InsertTagSerializer extends JsonSerializer<InsertTagChange> {

    private Page page;

    public InsertTagSerializer(Page page) {
        this.page = page;
    }

    @Override
    public void serialize(InsertTagChange change, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        HTMLElement newChild = change.getNewChild();
        Tag parent = change.getParentTag();
        jgen.writeStartObject();
        jgen.writeStringField("action", "appendTag");
        jgen.writeNumberField("parentId", parent.getId());
        jgen.writeNumberField("position", change.getPosition());
        StringWriter writer = new StringWriter();
        newChild.render(writer);
        jgen.writeStringField("content", writer.toString());
        jgen.writeEndObject();
    }
}