package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observer;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CharacterTest extends TestCase {

    private class TestObs implements Observer<ClientModel> {
        public ClientModel message;

        @Override
        public void update(ClientModel message) {
            this.message = message;
        }
    }
    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6,7,8,9,10,11})
    public void testGetPrice(int pos) {
        CharactersEnum c = CharactersEnum.values()[pos];
        int price = c.getPrice();
        Character character = new Character(0,c);
        assertEquals(character.getPrice(),price);
    }


    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6,7,8,9,10,11})
    public void testUse(int pos) {
        CharactersEnum c = CharactersEnum.values()[pos];
        int price = c.getPrice();
        Character character = new Character(0,c);
        TestObs obs = new TestObs();
        character.addObserver(obs);
        character.use();
        ClientCharacter message = (ClientCharacter)obs.message;
        assertEquals(price+1,character.getPrice());
        assertEquals(price+1,message.getPrice());
        assertEquals(0,message.getID());
        assertEquals(c,message.getCard());
        assertEquals(character.getUsed(),true);
        assertEquals(character.getCard(), c);
        character.use();
        message = (ClientCharacter)obs.message;
        assertEquals(price+1,character.getPrice());
        assertEquals(price+1,message.getPrice());
    }
}