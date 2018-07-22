package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.AttributeRemovedChange;

public class AttributeRemovedSerializer extends JsonSerializer<AttributeRemovedChange> {

    @Override
    public void serialize(AttributeRemovedChange change, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("action", "attributeRemoved");
        jgen.writeNumberField("elementId", change.getElementId());
        jgen.writeStringField("key", change.getKey());
        jgen.writeEndObject();
    }
}