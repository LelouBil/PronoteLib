package net.leloubil.pronotelib.entities;

import net.leloubil.pronotelib.PronoteConnection;
import net.leloubil.pronotelib.TestPronotConnection;
import org.junit.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HomeworkTest {

    @Test
    public void getHomeworkTest() throws IllegalAccessException{
        PronoteConnection comm = new PronoteConnection(TestPronotConnection.url);
        assertTrue("connexion",comm.login(TestPronotConnection.user, TestPronotConnection.pass));
        LocalDate date = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int wk = date.get(weekFields.weekOfWeekBasedYear());
        List<Homework> t = comm.getHomeworkList(wk + 17);
        //checkNull(t);
    }

    private void checkNull(Object o) throws IllegalAccessException{
        assertNotNull("EDT pas nul",o);
        for (Field f : o.getClass().getDeclaredFields()){
            f.setAccessible(true);
            assertNotNull(f.getName() + " pas nul dans EDT",f.get(o));
        }
    }
}
