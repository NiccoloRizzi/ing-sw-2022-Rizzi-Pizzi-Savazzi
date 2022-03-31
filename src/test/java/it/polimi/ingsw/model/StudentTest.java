package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class StudentTest extends TestCase {

    @Test
    public void testGetType() {
        Student s;
        for(Colour c : Colour.values())
        {
            s = new Student(c);
            assertEquals(c,s.getType());
        }
    }
}