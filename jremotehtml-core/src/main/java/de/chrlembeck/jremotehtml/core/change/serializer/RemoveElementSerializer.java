package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.RemoveElementChange;
import de.chrlembeck.jremotehtml.core.element.Tag;

public class RemoveElementSerializer extends JsonSerializer<RemoveElementChange> {

    @Override
    public void serialize(RemoveElementChange change, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        Tag parent = change.getParent();
        jgen.writeStartObject();
        jgen.writeStringField("action", "removeElement");
        jgen.writeNumberField("parentId", parent.getId());
        jgen.writeNumberField("position", change.getPosition());
        Integer childId = change.getId();
        if (childId != null) {
            jgen.writeNumberField("childId", childId);
        }
        jgen.writeEndObject();
    }
}