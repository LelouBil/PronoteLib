package net.leloubil.pronotelib.entities;

import net.leloubil.pronotelib.ObjetCommunication;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.SecurityException;
import static org.junit.Assert.*;
import net.leloubil.pronotelib.TestObjetCommunication;


public class EDTTest {

    @Test
    public void EDTGetTest() throws IllegalAccessException{
	ObjetCommunication comm = new ObjetCommunication(TestObjetCommunication.url);
        assertTrue("connexion",comm.identificate(TestObjetCommunication.user,TestObjetCommunication.pass));
        EDT t = comm.getEmploiDuTemps(1);
	    checkNull(t);
        System.out.println(t.toString());
    }

    public void checkNull(Object o) throws IllegalAccessException{
	    assertNotNull("EDT pas nul",o);
    	for (Field f : o.getClass().getDeclaredFields()){
		f.setAccessible(true);
        	assertNotNull(f.getName() + " pas nul dans EDT",f.get(o));
	}
    }
}
