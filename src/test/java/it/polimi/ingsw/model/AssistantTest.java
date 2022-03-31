package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

public class AssistantTest extends TestCase {

    @Test
    public void testGetValue() {
        int value = 1;
        Assistant a = new Assistant(value,0);
        assertEquals(value,a.getValue());
    }

    @Test
    public void testGetMn_moves() {
        int moves = 1;
        Assistant a = new Assistant(0,moves);
        assertEquals(moves,a.getMn_moves());
    }
}