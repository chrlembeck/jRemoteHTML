package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.TextModifiedChange;

public class TextModifiedSerializer extends JsonSerializer<TextModifiedChange> {

    @Override
    public void serialize(TextModifiedChange change, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("action", "textModified");
        jgen.writeNumberField("parentId", change.getParentId());
        jgen.writeNumberField("position", change.getPosition());
        jgen.writeStringField("text", change.getText());
        jgen.writeEndObject();
    }
}