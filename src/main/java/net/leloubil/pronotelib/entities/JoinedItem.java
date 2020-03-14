package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinedItem {

    @JsonProperty("L") @Getter
    String url;
}
