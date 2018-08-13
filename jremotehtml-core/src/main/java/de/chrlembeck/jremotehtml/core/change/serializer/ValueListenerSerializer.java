package de.chrlembeck.jremotehtml.core.change.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import de.chrlembeck.jremotehtml.core.change.ValueListenerChange;

public class ValueListenerSerializer extends JsonSerializer<ValueListenerChange> {

    @Override
	public void serialize(ValueListenerChange change, JsonGenerator jgen, SerializerProvider serializers)
            throws IOException {
        jgen.writeStartObject();
		jgen.writeStringField("action", "modifyValueListener");
        jgen.writeNumberField("elementId", change.getTag().getId());
        jgen.writeBooleanField("enabled", change.isEnabled());
        jgen.writeEndObject();
    }
}