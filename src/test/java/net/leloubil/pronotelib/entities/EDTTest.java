package net.leloubil.pronotelib.entities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import net.leloubil.pronotelib.PronoteConnection;
import net.leloubil.pronotelib.TestPronotConnection;
import net.leloubil.pronotelib.TestUtils;

public class EDTTest {

    @Test
    public void EDTGetTest() throws IllegalAccessException {
        PronoteConnection comm = new PronoteConnection(TestPronotConnection.url);
        assertTrue("connexion", comm.login(TestPronotConnection.user, TestPronotConnection.pass));
        EDT t = comm.getEmploiDuTemps(1);
        TestUtils.nestedNullCheck(t, "EDT");
        System.out.println(t.toString());
    }
}
