package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.StyleRemovedChange;

public class StyleRemovedSerializer extends JsonSerializer<StyleRemovedChange> {

    @Override
    public void serialize(StyleRemovedChange change, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("action", "styleRemoved");
        jgen.writeNumberField("elementId", change.getElementId());
        jgen.writeStringField("key", change.getKey());
        jgen.writeEndObject();
    }
}