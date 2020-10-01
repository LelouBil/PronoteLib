package net.leloubil.pronotelib;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestPronotConnection {

    public static final String user = "demonstration";
    public static final String pass = "pronotevs";
    public static final String url = "https://demo.index-education.net/pronote/eleve.html";

    @Test
    public void connectTest() {
        PronoteConnection obj = new PronoteConnection(url);
        assertTrue("connexion", obj.login(user, pass));
    }
}
