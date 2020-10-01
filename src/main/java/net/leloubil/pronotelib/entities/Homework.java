package net.leloubil.pronotelib.entities;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Homework {

    @JsonProperty("descriptif")
    String description;

    @JsonProperty("PourLe")
    private void unpackNestedDeadline(Map<String,Object> data) {
        this.deadline = DateDeserializer.getCalendar((String)data.get("V"));
    }
    Calendar deadline;

    @JsonProperty("TAFFait")
    boolean done;

    @JsonProperty("niveauDifficulte")
    int difficulty;

    @JsonProperty("duree")
    int duration;

    @JsonProperty("DonneLe.V")
    private void unpackNestedgivenDate(Map<String,Object> data) {
        this.givenDate =  DateDeserializer.getCalendar((String)data.get("V"));
    }
    Calendar givenDate;

    @JsonProperty("Matiere")
    private void unpackNestedMatiere(Map<String,Object> data) {
        this.lessonName = (String) ((Map<String, Object>) data.get("V")).get("L");
    }
    String lessonName;

    @JsonProperty("CouleurFond")
    String color;

    @JsonProperty("nomPublic")
    String publicName;

    @JsonProperty("ListePieceJointe")
    private void unpackNestedPieceJointe(Map<String,Object> data) {
        ObjectMapper mapper = new ObjectMapper();
        this.joinedItemList = new ArrayList<>();
        for (Map<String, Object> joined : (ArrayList<Map<String, Object>>) data.get("V")) {
            this.joinedItemList.add(mapper.convertValue(joined,JoinedItem.class));
        }
    }
    List<JoinedItem> joinedItemList;

}
