package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TileTest extends TestCase {

    @Test
    public void testAddStudent() {
        Tile t = new Tile ();
        Student s = new Student(Colour.Gnomes);
        t.addStudent(s);
        assertEquals(s,t.students.get(0));
    }

    @Test
    public void testAddStudents() {
        Tile t = new Tile ();
        int i = 0;
        ArrayList<Student> s = new ArrayList<Student>();
        for(Colour c: Colour.values())
            s.add(new Student(c));
        t.addStudents(s);
        for(Colour c: Colour.values()) {
            assertEquals(c, t.getStudent(i).getType());
            i++;
        }
    }
}