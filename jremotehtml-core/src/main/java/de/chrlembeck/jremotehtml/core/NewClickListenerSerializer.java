package de.chrlembeck.jremotehtml.core;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class NewClickListenerSerializer extends JsonSerializer<NewClickListener> {

	private Page page;

	public NewClickListenerSerializer(Page page) {
		this.page = page;
	}

	@Override
	public void serialize(NewClickListener value, JsonGenerator jgen, SerializerProvider serializers)
			throws IOException {
		jgen.writeStartObject();
		jgen.writeStringField("action", "newClickListener");
		jgen.writeStringField("elementId", value.getTag().getId(page));
		jgen.writeEndObject();
	}
}