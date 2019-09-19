package net.leloubil.pronotelib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_T",
        "V"
})
public class ClassData {

    public String teacher;

    public String className;

    public String room;


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("teacher", teacher).append("className", className).append("room", room).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(teacher).append(className).append(room).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ClassData)) {
            return false;
        }
        ClassData rhs = ((ClassData) other);
        return new EqualsBuilder().append(className, rhs.className).append(teacher, rhs.teacher).append(room, rhs.room).isEquals();
    }

}
