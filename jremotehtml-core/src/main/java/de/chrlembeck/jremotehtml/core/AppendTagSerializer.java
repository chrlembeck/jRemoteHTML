package de.chrlembeck.jremotehtml.core;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class AppendTagSerializer extends JsonSerializer<AppendTag> {

	private Page page;

	public AppendTagSerializer(Page page) {
		this.page = page;
	}

	@Override
	public void serialize(AppendTag appendTag, JsonGenerator jgen, SerializerProvider provider) throws IOException {
		HTMLElement newChild = appendTag.getNewChild();
		Tag parent = appendTag.getParentTag();
		jgen.writeStartObject();
		jgen.writeStringField("action", "appendTag");
		jgen.writeStringField("parentId", parent.getId(page));
		StringWriter writer = new StringWriter();
		newChild.render(page, writer);
		jgen.writeStringField("content", writer.toString());
		jgen.writeEndObject();
	}
}