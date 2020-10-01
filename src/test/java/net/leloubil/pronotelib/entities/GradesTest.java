package net.leloubil.pronotelib.entities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.leloubil.pronotelib.PronoteConnection;
import net.leloubil.pronotelib.TestPronotConnection;
import net.leloubil.pronotelib.TestUtils;

public class GradesTest {

    @Test
    public void GetGradesTest() throws IllegalAccessException {
        PronoteConnection comm = new PronoteConnection(TestPronotConnection.url);
        assertTrue("connexion", comm.login(TestPronotConnection.user, TestPronotConnection.pass));
        GradeData t = comm.getGrades(comm.getPeriodeList().get(0));
        TestUtils.nestedNullCheck(t, "notes");
        System.out.println(t.getListeDevoirs().get(0).getMatiere().getMoyEleve());
    }

}
