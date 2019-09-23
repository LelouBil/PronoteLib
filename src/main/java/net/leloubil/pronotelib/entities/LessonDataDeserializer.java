package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class LessonDataDeserializer extends StdDeserializer<LessonData> {

    public LessonDataDeserializer() {
        super(LessonData.class);
    }

    @Override
    public LessonData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ArrayNode node = (ArrayNode) p.getCodec().readTree(p).get("V");
        LessonData c = new LessonData();
        try {
            c.className = node.get(0).get("L").asText();

            c.teacher = node.get(1).get("L").asText();

            c.room = node.get(2).get("L").asText();
        } catch (NullPointerException ignored) {
        }
        return c;
    }
}
