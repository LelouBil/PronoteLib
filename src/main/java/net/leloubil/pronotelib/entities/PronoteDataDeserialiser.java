package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PronoteDataDeserialiser<T> extends StdDeserializer<T> {
    private static final long serialVersionUID = 5437210762900086277L;
    private Class<T> tclass;

    public PronoteDataDeserialiser(Class<T> tclass) {
        super(tclass);
        this.tclass = tclass;
    }

    @Override
    public T deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        ObjectMapper m = new ObjectMapper();
        //m.registerModule(mod);
        if (node.isContainerNode() && node.get("V") != null) node = node.get("V");
        if (node.isContainerNode() && node.get("L") != null) node = node.get("L");
        String text = node.asText();
        if (tclass == double.class || tclass == float.class) {
            node = m.valueToTree(text.replace(",", ".").replace("|", ""));
        }
        return m.treeToValue(node, tclass);
    }
}
