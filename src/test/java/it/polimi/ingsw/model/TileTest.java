package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TileTest extends TestCase {

    @Test
    public void testAddStudent() {
        Isle t = new Isle (0);
        t.addStudent(Colour.Gnomes);
        Assertions.assertEquals(1, (int)t.students.get(Colour.Gnomes));
    }

    @Test
    public void testAddStudents() {
        Isle t = new Isle (0);
        HashMap<Colour, Integer> s = new HashMap<>();
        for(Colour c: Colour.values())
            s.put(c, c.ordinal());
        t.addStudents(s);
        for(Colour c: Colour.values()) {
            Assertions.assertEquals(c.ordinal(), (int)t.students.get(c));
        }
    }
}