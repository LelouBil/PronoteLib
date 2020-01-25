package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "L",
        "N",
        "G",
        "ordre",
        "estServiceEnGroupe",
        "moyEleve",
        "baremeMoyEleve",
        "baremeMoyEleveParDefaut",
        "moyClasse",
        "moyMin",
        "moyMax",
        "couleur"
})
public class Matiere {

    @JsonProperty("L")
    private String name;
    @JsonProperty("estServiceEnGroupe")
    private boolean groupWork;
    @JsonProperty("moyEleve")
    private double moyEleve;
    @JsonProperty("baremeMoyEleve")
    private double baremeMoyEleve;
    @JsonProperty("moyClasse")
    private double moyClasse;
    @JsonProperty("moyMin")
    private double moyMin;
    @JsonProperty("moyMax")
    private double moyMax;
    @JsonProperty("couleur")
    private String couleur;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("L")
    public String getName() {
        return name;
    }


    @JsonProperty("estServiceEnGroupe")
    public boolean isGroupWork() {
        return groupWork;
    }

    @JsonProperty("moyEleve")
    public double getMoyEleve() {
        return moyEleve;
    }


    @JsonProperty("baremeMoyEleve")
    public double getBaremeMoyEleve() {
        return baremeMoyEleve;
    }


    @JsonProperty("moyClasse")
    public double getMoyClasse() {
        return moyClasse;
    }


    @JsonProperty("moyMin")
    public double getMoyMin() {
        return moyMin;
    }


    @JsonProperty("moyMax")
    public double getMoyMax() {
        return moyMax;
    }

    @JsonProperty("couleur")
    public String getCouleur() {
        return couleur;
    }


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}