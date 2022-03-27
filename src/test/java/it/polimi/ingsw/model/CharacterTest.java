package it.polimi.ingsw.model;

import junit.framework.TestCase;

public class CharacterTest extends TestCase {

    public void testGetPrice() {
        int price = 1;
        Character c = new Character(0,price);
        assertEquals(price,c.getPrice());
    }

    public void testGetId() {
        int id = 0;
        Character c = new Character(id,1);
        assertEquals(id,c.getId());
    }

    public void testUse() {
        int price = 1;
        Character c = new Character(0,price);
        c.use();
        assertEquals(price+1,c.getPrice());
        c.use();
        assertEquals(price+1,c.getPrice());
    }
}