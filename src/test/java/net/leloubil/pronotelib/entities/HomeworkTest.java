package net.leloubil.pronotelib.entities;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import net.leloubil.pronotelib.PronoteConnection;
import net.leloubil.pronotelib.TestPronotConnection;
import net.leloubil.pronotelib.TestUtils;

public class HomeworkTest {

    @Test
    public void getHomeworkTest() throws IllegalAccessException {
        PronoteConnection comm = new PronoteConnection(TestPronotConnection.url);
        assertTrue("connexion", comm.login(TestPronotConnection.user, TestPronotConnection.pass));
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int wk = date.get(weekFields.weekOfWeekBasedYear());
        List<Homework> t = comm.getHomeworkList(wk + 17);
        TestUtils.nestedNullCheck(t, "devoirs");
    }

}
