package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.AttributeModifiedChange;

public class AttributeModifiedSerializer extends JsonSerializer<AttributeModifiedChange> {

    @Override
    public void serialize(AttributeModifiedChange change, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("action", "attributeModified");
        jgen.writeNumberField("elementId", change.getElementId());
        jgen.writeStringField("key", change.getKey());
        String value = change.getValue();
        if (value != null) {
            jgen.writeStringField("value", value);
        }
        jgen.writeEndObject();
    }
}