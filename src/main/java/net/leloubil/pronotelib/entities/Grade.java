package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import net.leloubil.pronotelib.PronoteConnection;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "N",
        "G",
        "note",
        "bareme",
        "baremeParDefaut",
        "date",
        "service",
        "periode",
        "moyenne",
        "estEnGroupe",
        "noteMax",
        "noteMin",
        "commentaire",
        "coefficient"
})
public class Grade {

    @JsonProperty("note")
    private double note;
    @JsonProperty("bareme")
    private double bareme;
    @JsonProperty("date")
    private Date date;
    @JsonProperty("service")
    @JsonDeserialize(using = MatiereDeser.class)
    private Matiere matiere;
    @JsonProperty("periode")
    private String periode;
    @JsonProperty("moyenne")
    private double moyenne;
    @JsonProperty("estEnGroupe")
    private boolean estEnGroupe;
    @JsonProperty("noteMax")
    private double noteMax;
    @JsonProperty("noteMin")
    private double noteMin;
    @JsonProperty("commentaire")
    private String commentaire;
    @JsonProperty("coefficient")
    private int coefficient;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();


    @JsonProperty("note")
    public double getNote() {
        return note;
    }

    @JsonProperty("bareme")
    public double getBareme() {
        return bareme;
    }

    @JsonProperty("date")
    public Date getDate() {
        return date;
    }


    @JsonProperty("service")
    public Matiere getMatiere() {
        return matiere;
    }


    @JsonProperty("periode")
    public String getPeriode() {
        return periode;
    }


    @JsonProperty("moyenne")
    public double getMoyenne() {
        return moyenne;
    }


    @JsonProperty("estEnGroupe")
    public boolean getEstEnGroupe() {
        return estEnGroupe;
    }


    @JsonProperty("noteMax")
    public double getNoteMax() {
        return noteMax;
    }


    @JsonProperty("noteMin")
    public double getNoteMin() {
        return noteMin;
    }


    @JsonProperty("commentaire")
    public String getCommentaire() {
        return commentaire;
    }


    @JsonProperty("coefficient")
    public Integer getCoefficient() {
        return coefficient;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    private static class MatiereDeser extends JsonDeserializer<Matiere> {
        public MatiereDeser() {
        }

        @Override
        public Matiere deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            ObjectMapper om = new ObjectMapper();
            om.registerModule(PronoteConnection.staticModule);
            return om.treeToValue(p.getCodec().readTree(p).get("V"), Matiere.class);
        }
    }

    public void setMatiere(Matiere mat) {
        this.matiere = mat;
    }
}