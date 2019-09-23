package net.leloubil.pronotelib.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_T",
        "V"
})
public class LessonData {

    String teacher;

    public String className;

    public String room;


    //FIXME: You should probably use java builtin StringBuilder
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("teacher", teacher)
                .append("className", className)
                .append("room", room).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(teacher)
                .append(className)
                .append(room).toHashCode();
    }

    //FIXME: Useless
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LessonData)) {
            return false;
        }
        LessonData rhs = ((LessonData) other);
        return new EqualsBuilder()
                .append(className, rhs.className)
                .append(teacher, rhs.teacher)
                .append(room, rhs.room).isEquals();
    }

}
