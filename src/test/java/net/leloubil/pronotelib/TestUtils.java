package net.leloubil.pronotelib;

import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

public final class TestUtils {
    public static void nestedNullCheck(Object obj, String objName) {
        assertNotNull(objName + " pas nul", obj);
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                assertNotNull(objName + "#" + f.getName() + " pas nul", f.get(obj));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // Ignore silently restrictions
            }
        }
    }
}
