package it.polimi.ingsw.model;

import junit.framework.TestCase;

import java.util.ArrayList;

public class CharacterStudentsTest extends TestCase {

    public void testAddStudent() {
        CharacterStudents c = new CharacterStudents(0,1);
        Student s = new Student(Colour.Dragons);
        c.addStudent(s);
        assertEquals(s,c.getStudent(0));
    }

    public void testAddStudents() {
        CharacterStudents c = new CharacterStudents(0,1);
        ArrayList<Student> s = new ArrayList<Student>();
        for (Colour color: Colour.values())
            s.add(new Student(color));
        c.addStudents(s);
        for(int i = 0; i<s.size();i++)
            assertEquals(s.get(i),c.getStudent(i));
    }

    public void testRemoveStudent() {
        CharacterStudents c = new CharacterStudents(0,1);
        ArrayList<Student> s = new ArrayList<Student>();
        for (Colour color: Colour.values())
            s.add(new Student(color));
        c.addStudents(s);
        for(int i = 0; i<s.size();i++)
            assertEquals(s.get(i),c.removeStudent(0));
    }
}