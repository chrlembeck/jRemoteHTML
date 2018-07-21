package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.NewClickListenerChange;

public class NewClickListenerSerializer extends JsonSerializer<NewClickListenerChange> {

    @Override
    public void serialize(NewClickListenerChange value, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("action", "newClickListener");
        jgen.writeNumberField("elementId", value.getTag().getId());
        jgen.writeEndObject();
    }
}