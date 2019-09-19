package net.leloubil.pronotelib.data;

import net.leloubil.pronotelib.ObjetCommunication;
import org.junit.Test;

public class EDTTest {

    @Test
    public void EDTGetTest(){
        String user = "demonstration";
        String pass = "pronotevs";
        String url = "https://demo.index-education.net/pronote/eleve.html";
        ObjetCommunication.setUrl(url);
        ObjetCommunication.identificate(user,pass);
        System.out.println(ObjetCommunication.getEmploiDuTemps(1).toString());
    }
}
