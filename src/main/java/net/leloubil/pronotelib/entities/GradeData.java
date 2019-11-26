package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "moyGenerale",
        "moyGeneraleClasse",
        "baremeMoyGenerale",
        "baremeMoyGeneraleParDefaut",
        "avecDetailDevoir",
        "avecDetailService",
        "listeServices",
        "listeDevoirs"
})
public class GradeData {

    @JsonProperty("moyGenerale")
    private double moyGenerale;
    @JsonProperty("moyGeneraleClasse")
    private double moyGeneraleClasse;
    @JsonProperty("baremeMoyGenerale")
    private double baremeMoyGenerale;
    @JsonProperty("avecDetailDevoir")
    private boolean avecDetailDevoir;
    @JsonProperty("avecDetailService")
    private boolean avecDetailService;
    @JsonProperty("listeServices")
    @JsonDeserialize(using = MatiereListDeser.class)
    private List<Matiere> listeServices;
    @JsonProperty("listeDevoirs")
    @JsonDeserialize(using = GradeListDeser.class)
    private List<Grade> listeDevoirs;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("moyGenerale")
    public double getMoyGenerale() {
        return moyGenerale;
    }

    @JsonProperty("moyGeneraleClasse")
    public double getMoyGeneraleClasse() {
        return moyGeneraleClasse;
    }

    @JsonProperty("baremeMoyGenerale")
    public double getBaremeMoyGenerale() {
        return baremeMoyGenerale;
    }


    @JsonProperty("avecDetailDevoir")
    public Boolean getAvecDetailDevoir() {
        return avecDetailDevoir;
    }


    @JsonProperty("avecDetailService")
    public Boolean getAvecDetailService() {
        return avecDetailService;
    }

    @JsonProperty("listeServices")
    public List<Matiere> getListeServices() {
        return listeServices;
    }

    @JsonProperty("listeDevoirs")
    public List<Grade> getListeDevoirs() {
        return listeDevoirs;
    }

    @JsonProperty("listeDevoirs")
    @JsonSetter
    private void setListeDevoirs(List<Grade> devoirs) {
        //System.out.println("setcalled");
        List<Grade> tempdev = new ArrayList<>();
        devoirs.forEach(d -> {
            String matname = d.getMatiere().getName();
            d.setMatiere(listeServices.stream().filter((Matiere s) -> s.getName().equals(matname)).findFirst().orElse(d.getMatiere()));
            tempdev.add(d);
        });
        this.listeDevoirs = tempdev;
    }


    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


}