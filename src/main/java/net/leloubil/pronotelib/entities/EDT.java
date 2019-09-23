package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

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
    private List<Lesson> lessons;

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
    public List<Lesson> getLessons() {
        return lessons;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("avecCoursAnnule", avecCoursAnnule)
                .append("parametreExportiCal", parametreExportiCal)
                .append("avecExportICal", avecExportICal)
                .append("prefsGrille", genreRessource)
                .append("listeCours", lessons)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(parametreExportiCal)
                .append(lessons)
                .append(genreRessource)
                .append(avecExportICal)
                .append(avecCoursAnnule)
                .toHashCode();
    }

    //FIXME: Useless
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof EDT)) {
            return false;
        }
        EDT rhs = ((EDT) other);
        return new EqualsBuilder().append(parametreExportiCal, rhs.parametreExportiCal).append(lessons, rhs.lessons).append(genreRessource, rhs.genreRessource).append(avecExportICal, rhs.avecExportICal).append(avecCoursAnnule, rhs.avecCoursAnnule).isEquals();
    }

}
