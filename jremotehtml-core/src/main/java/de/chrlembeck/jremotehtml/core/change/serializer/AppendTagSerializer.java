package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.AppendTagChange;
import de.chrlembeck.jremotehtml.core.element.HTMLElement;
import de.chrlembeck.jremotehtml.core.element.Page;
import de.chrlembeck.jremotehtml.core.element.Tag;

public class AppendTagSerializer extends JsonSerializer<AppendTagChange> {

    private Page page;

    public AppendTagSerializer(Page page) {
        this.page = page;
    }

    @Override
    public void serialize(AppendTagChange appendTag, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        HTMLElement newChild = appendTag.getNewChild();
        Tag parent = appendTag.getParentTag();
        jgen.writeStartObject();
        jgen.writeStringField("action", "appendTag");
        jgen.writeNumberField("parentId", parent.getId());
        StringWriter writer = new StringWriter();
        newChild.render(writer);
        jgen.writeStringField("content", writer.toString());
        jgen.writeEndObject();
    }
}