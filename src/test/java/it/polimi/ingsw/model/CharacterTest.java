package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class CharacterTest extends TestCase {

    @Test
    public void testGetPrice() {
        int price = 1;
        Character c = new Character(0,price);
        assertEquals(price,c.getPrice());
    }

    @Test
    public void testGetId() {
        int id = 0;
        Character c = new Character(id,1);
        assertEquals(id,c.getId());
    }

    @Test
    public void testUse() {
        int price = 1;
        Character c = new Character(0,price);
        c.use();
        assertEquals(price+1,c.getPrice());
        c.use();
        assertEquals(price+1,c.getPrice());
    }
}