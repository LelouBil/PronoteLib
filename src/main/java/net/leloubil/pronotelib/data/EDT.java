package net.leloubil.pronotelib.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EDT {

    @JsonProperty("avecCoursAnnule")
    private boolean avecCoursAnnule;
    @JsonProperty("ParametreExportiCal")
    private String parametreExportiCal;
    @JsonProperty("avecExportICal")
    private boolean avecExportICal;
    @JsonProperty("prefsGrille")
    private int genreRessource;
    @JsonProperty("ListeCours")
    private List<Cour> cours;

    @JsonProperty("avecCoursAnnule")
    public boolean avecCoursAnnule() {
        return avecCoursAnnule;
    }

    @JsonProperty("ParametreExportiCal")
    public String getParametreExportiCal() {
        return parametreExportiCal;
    }

    @JsonProperty("avecExportICal")
    public boolean avecExportICal() {
        return avecExportICal;
    }


    @JsonProperty("prefsGrille")
    public int getGenreRessource() {
        return genreRessource;
    }

    @JsonProperty("ListeCours")
    public List<Cour> getCours() {
        return cours;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("avecCoursAnnule", avecCoursAnnule).append("parametreExportiCal", parametreExportiCal).append("avecExportICal", avecExportICal).append("prefsGrille", genreRessource).append("listeCours", cours).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(parametreExportiCal).append(cours).append(genreRessource).append(avecExportICal).append(avecCoursAnnule).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EDT)) {
            return false;
        }
        EDT rhs = ((EDT) other);
        return new EqualsBuilder().append(parametreExportiCal, rhs.parametreExportiCal).append(cours, rhs.cours).append(genreRessource, rhs.genreRessource).append(avecExportICal, rhs.avecExportICal).append(avecCoursAnnule, rhs.avecCoursAnnule).isEquals();
    }

}
