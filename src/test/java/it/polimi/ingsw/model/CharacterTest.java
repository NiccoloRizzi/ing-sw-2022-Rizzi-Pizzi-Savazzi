package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CharacterTest extends TestCase {

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6,7,8,9,10,11})
    public void testGetPrice(int pos) {
        CharactersEnum c = CharactersEnum.values()[pos];
        int price = c.getPrice();
        Character character = new Character(c);
        assertEquals(character.getPrice(),price);
    }


    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6,7,8,9,10,11})
    public void testUse(int pos) {
        CharactersEnum c = CharactersEnum.values()[pos];
        int price = c.getPrice();
        Character character = new Character(c);
        character.use();
        assertEquals(price+1,character.getPrice());
        character.use();
        assertEquals(price+1,character.getPrice());
    }
}