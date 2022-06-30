package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.server.Observer;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

public class CharacterStudentsTest extends TestCase {
    private static class TestObs implements Observer<ClientModel> {
        public ClientModel message;

        @Override
        public void update(ClientModel message) {
            this.message = message;
        }
    }
    @Test
    public void testAddStudent() {
        CharactersEnum c = CharactersEnum.ONE_STUD_TO_ISLE;
        CharacterStudents character = new CharacterStudents(0,c);
        TestObs obs = new TestObs();
        character.addObserver(obs);
        character.addStudent(Colour.Dragons);
        ClientCharacter message = (ClientCharacter)obs.message;
        assertEquals(1,character.getStudents(Colour.Dragons));
        assertEquals(1,(int)message.getStudents().get(Colour.Dragons));
    }

    @Test
    public void testAddRemoveStudents() {
        CharactersEnum character = CharactersEnum.ONE_STUD_TO_ISLE;
        CharacterStudents cs = new CharacterStudents(0,character);
        TestObs obs = new TestObs();
        cs.addObserver(obs);
        HashMap<Colour, Integer> s = new HashMap<>();
        for (Colour color: Colour.values())
            s.put(color, 1);

        cs.addStudents(s);
        ClientCharacter message = (ClientCharacter)obs.message;
        for(Colour c: Colour.values()) {
            assertEquals(s.get(c).intValue(), cs.getStudents(c));
            assertEquals(s.get(c),message.getStudents().get(c));
        }

        for(Colour c: Colour.values()){
            try {
                cs.removeStudent(c);
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        message = (ClientCharacter)obs.message;
        for(Colour c: Colour.values()) {
            assertEquals(0, cs.getStudents(c));
            assertEquals(0,(int)message.getStudents().get(c));
        }

        assertThrowsExactly(StudentsOutOfBoundsException.class, ()->cs.removeStudent(Colour.Dragons));
    }

}