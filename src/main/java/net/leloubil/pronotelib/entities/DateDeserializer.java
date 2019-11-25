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

    private static Calendar getCalendar(String date) {
        String[] splat = date.split(" ");
        String[] day = splat[0].split("/");
        if (splat.length == 2) {
            String[] hour = date.split(" ")[1].split(":");
            //noinspection MagicConstant
            return new GregorianCalendar(Integer.parseInt(day[2]), Integer.parseInt(day[1]), Integer.parseInt(day[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]), Integer.parseInt(hour[2]));
        } else {
            //noinspection MagicConstant
            return new GregorianCalendar(Integer.parseInt(day[2]), Integer.parseInt(day[1]), Integer.parseInt(day[0]));
        }
    }

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode n = p.getCodec().readTree(p);

        String date = n.get("V").asText().replace("\\", "");

        return getCalendar(date).getTime();
    }
}
