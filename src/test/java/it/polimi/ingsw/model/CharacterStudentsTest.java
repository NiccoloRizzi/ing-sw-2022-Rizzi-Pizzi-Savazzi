package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CharacterStudentsTest extends TestCase {

    @Test
    public void testAddStudent() {
        CharacterStudents c = new CharacterStudents(0,1);
        c.addStudent(Colour.Dragons);
        assertEquals(1,c.getStudents(Colour.Dragons));
    }

    @Test
    public void testAddRemoveStudents() {
        CharacterStudents cs = new CharacterStudents(0,1);
        HashMap<Colour, Integer> s = new HashMap<>();
        for (Colour color: Colour.values())
            s.put(color, 1);

        cs.addStudents(s);
        for(Colour c: Colour.values())
            assertEquals(s.get(c).intValue() , cs.getStudents(c));

        for(Colour c: Colour.values()){
            try {
                cs.removeStudent(c);
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        for(Colour c: Colour.values()) {
            assertEquals(0, cs.getStudents(c));
        }

        assertThrowsExactly(StudentsOutOfBoundsException.class, ()->cs.removeStudent(Colour.Dragons));
    }

}