package it.polimi.ingsw.model;

import junit.framework.TestCase;

public class StudentTest extends TestCase {

    public void testGetType() {
        Student s;
        for(Colour c : Colour.values())
        {
            s = new Student(c);
            assertEquals(c,s.getType());
        }
    }
}