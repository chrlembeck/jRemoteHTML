package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.StyleModifiedChange;

public class StyleModifiedSerializer extends JsonSerializer<StyleModifiedChange> {

    @Override
    public void serialize(StyleModifiedChange change, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("action", "styleModified");
        jgen.writeNumberField("elementId", change.getElementId());
        jgen.writeStringField("key", change.getKey());
        String value = change.getValue();
        if (value != null) {
            jgen.writeStringField("value", value);
        }
        jgen.writeEndObject();
    }
}