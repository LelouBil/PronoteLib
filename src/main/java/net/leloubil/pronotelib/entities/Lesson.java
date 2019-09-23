package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.leloubil.pronotelib.ObjetCommunication;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "N",
        "G",
        "P",
        "place",
        "duree",
        "DateDuCours",
        "CouleurFond",
        "ListeContenus",
        "AvecTafPublie",
        "cahierDeTextes",
        "Statut",
        "estAnnule"
})
public class Lesson {

    @JsonProperty("N")
    private String id;
    @JsonProperty("G")
    private Integer g; //todo
    @JsonProperty("P")
    private Integer p; //todo
    @JsonProperty("place")
    private Integer place; //todo
    @JsonProperty("duree")
    private int duration;
    @JsonProperty("DateDuCours")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date LessonDate;
    @JsonProperty("CouleurFond")
    private String backgroundColor;
    private LessonData lessonData;
    @JsonProperty("AvecTafPublie")
    private boolean withLesson;
    @JsonProperty("cahierDeTextes")
    @JsonDeserialize(using = CDTDeser.class)
    private String homeworkID;
    @JsonProperty("Statut")
    private String status;
    @JsonProperty("estAnnule")
    private boolean isCancelled;

    //Deobfuscate
    @JsonProperty("N")
    public String getId() {
        return id;
    }

    //Deobfuscate
    @JsonProperty("G")
    public Integer getG() {
        return g;
    }

    //Deobfuscate
    @JsonProperty("G")
    public void setG(Integer g) {
        this.g = g;
    }

    //Deobfuscate
    @JsonProperty("P")
    public Integer getP() {
        return p;
    }


    @JsonProperty("place")
    public Integer getPlace() {
        return place;
    }

    /**
     * Lesson duration, in FIXME document unit.
     *
     * @return An {@link Integer} representing the lesson duration in FIXME document unit.
     */
    @JsonProperty("duree")
    public Integer getDuration() {
        return duration;
    }

    /**
     * The {@link Date} at which the lesson should occur.
     * @return A {@link Date} representing the date at which the lesson should occur.
     */
    @JsonProperty("DateDuCours")
    public Date getLessonDate() {
        return LessonDate;
    }

    @JsonProperty("CouleurFond")
    public String getBackgroundColor() {
        return backgroundColor;
    }


    @JsonProperty("ListeContenus")
    public LessonData getListeContenus() {
        return lessonData;
    }

    /**
     * Whichever this lesson has homework associated whith it.
     * @return {@code true} if this lesson has homework, {@code false} otherwise.
     */
    @JsonProperty("AvecTafPublie")
    public boolean hasHomework() {
        return withLesson;
    }


    @JsonProperty("cahierDeTextes")
    public String getHomeworkID() {
        return homeworkID;
    }

    @JsonProperty("Statut")
    public String getStatus() {
        return status;
    }

    @JsonProperty("estAnnule")
    public Boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("g", g).append("p", p).append("place", place).append("duree", duration).append("dateDuCours", LessonDate).append("couleurFond", backgroundColor).append("lessonData", lessonData).append("avecDevoirs", withLesson).append("devoirId", homeworkID).append("statut", status).append("estAnnule", isCancelled).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(g).append(status).append(homeworkID).append(id).append(withLesson).append(LessonDate).append(p).append(isCancelled).append(backgroundColor).append(duration).append(place).append(lessonData).toHashCode();
    }

    //FIXME: Useless
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Lesson)) {
            return false;
        }
        Lesson rhs = ((Lesson) other);
        return new EqualsBuilder().append(g, rhs.g).append(status, rhs.status).append(homeworkID, rhs.homeworkID).append(id, rhs.id).append(withLesson, rhs.withLesson).append(LessonDate, rhs.LessonDate).append(p, rhs.p).append(isCancelled, rhs.isCancelled).append(backgroundColor, rhs.backgroundColor).append(duration, rhs.duration).append(place, rhs.place).append(lessonData, rhs.lessonData).isEquals();
    }

    public static class CDTDeser extends StdDeserializer<String> {
        public CDTDeser() {
            super(String.class);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode n = p.getCodec().readTree(p);
            return n.get("V").asText();
        }
    }

    public static class LessonDeserializer extends StdDeserializer<Lesson> {

        private ObjetCommunication link;
        public LessonDeserializer(ObjetCommunication obj) {
            super(Lesson.class);
            link = obj;
        }

        @Override
        public Lesson deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);

            String id = node.get("N").asText();
            String json = "{" +
                    "avecListeEleves: false," +
                    "avecNbEleves: false," +
                    "numeroSemaine: 3" +
                    "}";
            ObjectMapper om = new ObjectMapper();
            om.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            om.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            Map map = om.readValue(json, Map.class);
            map.put("cours", Collections.singletonMap("N", id));
            Lesson c = om.treeToValue(node, Lesson.class);
            JsonNode cour = link.appelFonction("FicheCours", map).get("donneesSec").get("donnees");
            cour = cour.get("listeCours").get("V").get(0);

            c.withLesson = cour.get("avecCDT").asBoolean();
            LessonData d = new LessonData();
            d.className = cour.get("matiere").get("V").get("L").asText();
            d.teacher = cour.get("ListeContenus").get("V").get(0).get("L").asText();
            if (cour.get("ListeContenus").get("V").size() >= 2)
                d.room = cour.get("ListeContenus").get("V").get(1).get("L").asText();
            c.lessonData = d;
            String date = cour.get("DateDuCours").get("V").asText().replace("\\", "");

            String[] day = date.split(" ")[0].split("/");
            String[] hour = date.split(" ")[1].split(":");

            Calendar ca = new GregorianCalendar(Integer.parseInt(day[2]), Integer.parseInt(day[1]), Integer.parseInt(day[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]), Integer.parseInt(hour[2]));
            c.LessonDate = ca.getTime();
            return c;
        }
    }
}
