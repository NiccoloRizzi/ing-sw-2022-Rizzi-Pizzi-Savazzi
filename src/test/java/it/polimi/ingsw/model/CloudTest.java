package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class CloudTest extends TestCase {

    @Test
    public void testEmpty() {
        Cloud cloud = new Cloud (0);
        HashMap<Colour,Integer> s = new HashMap<Colour,Integer>();
        for(Colour c: Colour.values())
            s.put(c,1);
        assertTrue(cloud.isEmpty());
        cloud.addStudents(s);
        assertFalse(cloud.isEmpty());
        HashMap<Colour,Integer> s1 = cloud.empty();
        assertTrue(cloud.isEmpty());
        assertEquals(s.size(),s1.size());
        for(Colour c: Colour.values())
            assertEquals(s.get(c),s1.get(c));

    }
}