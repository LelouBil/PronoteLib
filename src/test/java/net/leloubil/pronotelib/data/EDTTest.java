package net.leloubil.pronotelib.data;

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
        assert(comm.identificate(TestObjetCommunication.user,TestObjetCommunication.pass));
        EDT t = comm.getEmploiDuTemps(1);
	checkNull(t);
    }

    public void checkNull(Object o) throws IllegalAccessException{
	assertNotNull(o);
    	for (Field f : o.getClass().getDeclaredFields()){
		f.setAccessible(true);
		System.out.println(f.getName());
        	assertNotNull(f.get(o));
	}
    }
}
