package it.polimi.ingsw.model;

import junit.framework.TestCase;

public class AssistantTest extends TestCase {

    public void testGetValue() {
        int value = 1;
        Assistant a = new Assistant(value,0);
        assertEquals(value,a.getValue());
    }

    public void testGetMn_moves() {
        int moves = 1;
        Assistant a = new Assistant(0,moves);
        assertEquals(moves,a.getMn_moves());
    }
}