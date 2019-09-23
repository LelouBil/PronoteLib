package net.leloubil.pronotelib;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestObjetCommunication{

	public static final String user = "demonstration";
        public static final String pass = "pronotevs";
        public static final String url = "https://demo.index-education.net/pronote/eleve.html";

	@Test
	public void connectTest(){
		ObjetCommunication obj = new ObjetCommunication(url);
		assertTrue("connexion",obj.identificate(user,pass));
	}
}
