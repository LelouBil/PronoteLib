package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Periode {

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    @JsonProperty("L")
    private String name;

    @JsonProperty("G")
    private int number;

    @JsonProperty("N")
    private String id;
}
