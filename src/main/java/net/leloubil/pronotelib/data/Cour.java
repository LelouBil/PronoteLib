package net.leloubil.pronotelib.data;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import net.leloubil.pronotelib.ObjetCommunication;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
public class Cour {

    @JsonProperty("N")
    private String id;
    @JsonProperty("G")
    private Integer g; //todo
    @JsonProperty("P")
    private Integer p; //todo
    @JsonProperty("place")
    private Integer place; //todo
    @JsonProperty("duree")
    private int duree;
    @JsonProperty("DateDuCours") @JsonDeserialize(using = DateDeserializer.class)
    private Date dateDuCours;
    @JsonProperty("CouleurFond")
    private String couleurFond;
    private ClassData classData;
    @JsonProperty("AvecTafPublie")
    private boolean avecDevoirs;
    @JsonProperty("cahierDeTextes") @JsonDeserialize(using = CDTDeser.class)
    private String devoirId;
    @JsonProperty("Statut")
    private String statut;
    @JsonProperty("estAnnule")
    private boolean estAnnule;
    @JsonProperty("N")
    public String getId() {
        return id;
    }

    @JsonProperty("G")
    public Integer getG() {
        return g;
    }

    @JsonProperty("G")
    public void setG(Integer g) {
        this.g = g;
    }

    @JsonProperty("P")
    public Integer getP() {
        return p;
    }



    @JsonProperty("place")
    public Integer getPlace() {
        return place;
    }



    @JsonProperty("duree")
    public Integer getDuree() {
        return duree;
    }


    @JsonProperty("DateDuCours")
    public Date getDateDuCours() {
        return dateDuCours;
    }


    @JsonProperty("CouleurFond")
    public String getCouleurFond() {
        return couleurFond;
    }


    @JsonProperty("ListeContenus")
    public ClassData getListeContenus() {
        return classData;
    }

    @JsonProperty("AvecTafPublie")
    public boolean hasDevoir() {
        return avecDevoirs;
    }


    @JsonProperty("cahierDeTextes")
    public String getDevoirId() {
        return devoirId;
    }

    @JsonProperty("Statut")
    public String getStatut() {
        return statut;
    }

    @JsonProperty("estAnnule")
    public Boolean getEstAnnule() {
        return estAnnule;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("g", g).append("p", p).append("place", place).append("duree", duree).append("dateDuCours", dateDuCours).append("couleurFond", couleurFond).append("classData", classData).append("avecDevoirs", avecDevoirs).append("devoirId", devoirId).append("statut", statut).append("estAnnule", estAnnule).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(g).append(statut).append(devoirId).append(id).append(avecDevoirs).append(dateDuCours).append(p).append(estAnnule).append(couleurFond).append(duree).append(place).append(classData).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Cour)) {
            return false;
        }
        Cour rhs = ((Cour) other);
        return new EqualsBuilder().append(g, rhs.g).append(statut, rhs.statut).append(devoirId, rhs.devoirId).append(id, rhs.id).append(avecDevoirs, rhs.avecDevoirs).append(dateDuCours, rhs.dateDuCours).append(p, rhs.p).append(estAnnule, rhs.estAnnule).append(couleurFond, rhs.couleurFond).append(duree, rhs.duree).append(place, rhs.place).append(classData, rhs.classData).isEquals();
    }

    public static class CDTDeser extends StdDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode n = p.getCodec().readTree(p);
            return n.get("V").asText();
        }


        public CDTDeser(){super(String.class);}
    }

    public static class CoursDeserializer extends StdDeserializer<Cour> {
        ObjetCommunication link;
	@Override
        public Cour deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
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
            map.put("cours", Collections.singletonMap("N",id));
            Cour c = om.treeToValue(node,Cour.class);
            JsonNode cour = link.appelFonction("FicheCours",map).get("donneesSec").get("donnees");
            cour = cour.get("listeCours").get("V").get(0);

            c.avecDevoirs = cour.get("avecCDT").asBoolean();
            ClassData d = new ClassData();
            d.className = cour.get("matiere").get("V").get("L").asText();
            d.teacher = cour.get("ListeContenus").get("V").get(0).get("L").asText();
            if(cour.get("ListeContenus").get("V").size() >= 2) d.room = cour.get("ListeContenus").get("V").get(1).get("L").asText();
            c.classData = d;
            String date = cour.get("DateDuCours").get("V").asText().replace("\\","");

            String[] day = date.split(" ")[0].split("/");
            String[] hour = date.split(" ")[1].split(":");

            Calendar ca = new GregorianCalendar(Integer.parseInt(day[2]),Integer.parseInt(day[1]),Integer.parseInt(day[0]),Integer.parseInt(hour[0]),Integer.parseInt(hour[1]),Integer.parseInt(hour[2]));
            c.dateDuCours = ca.getTime();
            return c;
        }

        public CoursDeserializer(ObjetCommunication ln){
            super(Cour.class);
	    link = ln;
        }
    }
}
