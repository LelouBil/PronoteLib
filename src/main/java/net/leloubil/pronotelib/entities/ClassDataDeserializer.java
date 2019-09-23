package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

public class ClassDataDeserializer extends StdDeserializer<ClassData> {

    public ClassDataDeserializer() {
        super(ClassData.class);
    }

    @Override
    public ClassData deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ArrayNode node = (ArrayNode) p.getCodec().readTree(p).get("V");
        ClassData c = new ClassData();
        try {
            c.className = node.get(0).get("L").asText();

            c.teacher = node.get(1).get("L").asText();

            c.room = node.get(2).get("L").asText();
        } catch (NullPointerException ignored) {
        }
        return c;
    }
}
