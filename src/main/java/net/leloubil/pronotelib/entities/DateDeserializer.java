package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateDeserializer extends StdDeserializer<Date> {
    public DateDeserializer() {
        super(Date.class);
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode n = p.getCodec().readTree(p);

        String date = n.get("V").asText().replace("\\", "");

        String[] day = date.split(" ")[0].split("/");
        String[] hour = date.split(" ")[1].split(":");

        Calendar c = new GregorianCalendar(Integer.parseInt(day[2]), Integer.parseInt(day[1]), Integer.parseInt(day[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]), Integer.parseInt(hour[2]));
        return c.getTime();
    }
}
