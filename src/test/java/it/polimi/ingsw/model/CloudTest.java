package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCloud;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observer;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class CloudTest extends TestCase {

    private static class TestObs implements Observer<ClientModel> {
        public ClientModel message;

        @Override
        public void update(ClientModel message) {
            this.message = message;
        }
    }
    @Test
    public void testEmpty() {
        Cloud cloud = new Cloud (0);
        TestObs obs = new TestObs();
        cloud.addObserver(obs);
        HashMap<Colour,Integer> s = new HashMap<>();
        for(Colour c: Colour.values())
            s.put(c,1);
        assertTrue(cloud.isEmpty());
        cloud.addStudents(s);
        ClientCloud message = (ClientCloud)obs.message;
        assertFalse(cloud.isEmpty());
        for(Colour c: Colour.values())
            assertEquals(message.getStudents().get(c),s.get(c));
        HashMap<Colour,Integer> s1 = cloud.empty();
        message = (ClientCloud)obs.message;
        assertTrue(cloud.isEmpty());
        assertEquals(s.size(),s1.size());
        for(Colour c: Colour.values()) {
            assertEquals(s.get(c), s1.get(c));
            assertEquals((int)message.getStudents().get(c),0);
        }

    }
}