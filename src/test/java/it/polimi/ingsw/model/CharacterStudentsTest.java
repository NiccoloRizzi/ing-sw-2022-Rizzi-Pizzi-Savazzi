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
        CharactersEnum c = CharactersEnum.ONE_STUD_TO_ISLE;
        CharacterStudents character = new CharacterStudents(c);
        character.addStudent(Colour.Dragons);
        assertEquals(1,character.getStudents(Colour.Dragons));
    }

    @Test
    public void testAddRemoveStudents() {
        CharactersEnum character = CharactersEnum.ONE_STUD_TO_ISLE;
        CharacterStudents cs = new CharacterStudents(character);
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