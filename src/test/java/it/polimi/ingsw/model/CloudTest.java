package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CloudTest extends TestCase {

    @Test
    public void testEmpty() {
        Cloud cloud = new Cloud ();
        ArrayList<Student> s = new ArrayList<Student>();
        for(Colour c: Colour.values())
            s.add(new Student(c));
        cloud.addStudents(s);
        ArrayList<Student> s1 = cloud.empty();
        assertTrue(cloud.students.isEmpty());
        assertEquals(s.size(),s1.size());
        for(int i = 0; i < s.size();i++)
            assertEquals(s.get(i),s1.get(i));

    }
}