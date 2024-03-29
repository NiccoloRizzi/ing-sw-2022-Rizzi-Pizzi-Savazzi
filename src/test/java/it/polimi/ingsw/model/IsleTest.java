package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientIsle;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observer;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class IsleTest extends TestCase {


    private static class TestObs implements Observer<ClientModel> {
        public ClientModel message;

        @Override
        public void update(ClientModel message) {
            this.message = message;
        }
    }
    @Test
    public void testTower() {
        Isle isle = new Isle(0);
        TestObs obs = new TestObs();
        isle.addObserver(obs);
        assertEquals(Faction.Empty,isle.getTower());
        isle.setTower(Faction.Black);
        ClientIsle message = (ClientIsle)obs.message;
        assertEquals(Faction.Black,isle.getTower());
    }

    @Test
    public void testGetInfluenceDefault() {
        Isle isle = new Isle(0);
        isle.setTower(Faction.Black);
        HashMap<Colour,Integer> s = new HashMap<>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
            s.put(c,numStudent[c.ordinal()]);
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<>();
        Player p1 = new Player(1,"pippo");
        p1.createBoard(9);
        p1.assignFaction(Faction.Black);
        Player p2 = new Player(2,"pippo");
        p2.createBoard(9);
        p2.assignFaction(Faction.Grey);
        Player p3 = new Player(3,"pippo");
        p3.createBoard(9);
        p3.assignFaction(Faction.Black);
        Team t = new Team();
        t.addPlayer(p1);
        t.addPlayer(p3);
        t.assignFaction(Faction.Black);
        professors.put(Colour.Gnomes,p1);
        professors.put(Colour.Dragons,p1);
        professors.put(Colour.Frogs,p2);
        professors.put(Colour.Fairies,p2);
        professors.put(Colour.Unicorns,p3);
        assertEquals(numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()]+1,isle.getInfluence(p1,professors));
        assertEquals(numStudent[Colour.Frogs.ordinal()]+numStudent[Colour.Fairies.ordinal()],isle.getInfluence(p2,professors));
        assertEquals(numStudent[Colour.Unicorns.ordinal()]+numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()]+1,isle.getInfluence(t,professors));
    }

    @Test
    public void testGetInfluenceNoTower() {
        Isle isle = new Isle(0);
        isle.setInfStrategy(new noTowersStrategy());
        isle.setTower(Faction.Black);
        HashMap<Colour,Integer> s = new HashMap<>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
            s.put(c,numStudent[c.ordinal()]);
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<>();
        Player p1 = new Player(1,"pippo");
        p1.createBoard(9);
        p1.assignFaction(Faction.Black);
        Player p2 = new Player(2,"pippo");
        p2.createBoard(9);
        p2.assignFaction(Faction.Grey);
        Player p3 = new Player(3,"pippo");
        p3.createBoard(9);
        p3.assignFaction(Faction.Black);
        Team t = new Team();
        t.addPlayer(p1);
        t.addPlayer(p3);
        t.assignFaction(Faction.Black);
        professors.put(Colour.Gnomes,p1);
        professors.put(Colour.Dragons,p1);
        professors.put(Colour.Frogs,p2);
        professors.put(Colour.Fairies,p2);
        professors.put(Colour.Unicorns,p3);
        assertEquals(numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()],isle.getInfluence(p1,professors));
        assertEquals(numStudent[Colour.Frogs.ordinal()]+numStudent[Colour.Fairies.ordinal()],isle.getInfluence(p2,professors));
        assertEquals(numStudent[Colour.Unicorns.ordinal()]+numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()],isle.getInfluence(t,professors));
    }

    @Test
    public void testGetInfluencePlusInf() {
        Isle isle = new Isle(0);
        isle.setInfStrategy(new PlusInfStrategy(1));
        isle.setTower(Faction.Black);
        HashMap<Colour,Integer> s = new HashMap<>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
                s.put(c,numStudent[c.ordinal()]);
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<>();
        Player p1 = new Player(1,"pippo");
        p1.createBoard(9);
        p1.assignFaction(Faction.Black);
        Player p2 = new Player(2,"pippo");
        p2.createBoard(9);
        p2.assignFaction(Faction.Grey);
        Player p3 = new Player(3,"pippo");
        p3.createBoard(9);
        p3.assignFaction(Faction.Black);
        Team t = new Team();
        t.addPlayer(p1);
        t.addPlayer(p3);
        t.assignFaction(Faction.Black);
        professors.put(Colour.Gnomes,p1);
        professors.put(Colour.Dragons,p1);
        professors.put(Colour.Frogs,p2);
        professors.put(Colour.Fairies,p2);
        professors.put(Colour.Unicorns,p3);
        assertEquals(numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()]+1+2,isle.getInfluence(p1,professors));
        assertEquals(numStudent[Colour.Frogs.ordinal()]+numStudent[Colour.Fairies.ordinal()],isle.getInfluence(p2,professors));
        assertEquals(numStudent[Colour.Unicorns.ordinal()]+numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()]+1+2,isle.getInfluence(t,professors));
    }

    @Test
    public void testGetInfluenceNoColour() {
        Isle isle = new Isle(0);
        isle.setTower(Faction.Black);
        isle.setInfStrategy(new NoColourStrategy(Colour.Dragons));
        HashMap<Colour,Integer> s = new HashMap<>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
            s.put(c,numStudent[c.ordinal()]);
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<>();
        Player p1 = new Player(1,"pippo");
        p1.createBoard(9);
        p1.assignFaction(Faction.Black);
        Player p2 = new Player(2,"pippo");
        p2.createBoard(9);
        p2.assignFaction(Faction.Grey);
        Player p3 = new Player(3,"pippo");
        p3.createBoard(9);
        p3.assignFaction(Faction.Black);
        Team t = new Team();
        t.addPlayer(p1);
        t.addPlayer(p3);
        t.assignFaction(Faction.Black);
        professors.put(Colour.Gnomes,p1);
        professors.put(Colour.Dragons,p1);
        professors.put(Colour.Frogs,p2);
        professors.put(Colour.Fairies,p2);
        professors.put(Colour.Unicorns,p3);
        assertEquals(numStudent[Colour.Gnomes.ordinal()]+1,isle.getInfluence(p1,professors));
        assertEquals(numStudent[Colour.Frogs.ordinal()]+numStudent[Colour.Fairies.ordinal()],isle.getInfluence(p2,professors));
        assertEquals(numStudent[Colour.Unicorns.ordinal()]+numStudent[Colour.Gnomes.ordinal()]+1,isle.getInfluence(t,professors));
    }

    @Test
    public void testProhibited() {
        Isle isle = new Isle(0);
        assertFalse(isle.removeProhibited());
        isle.setProhibited();
        isle.setProhibited();
        assertTrue(isle.removeProhibited());
        isle.removeProhibited();
        assertFalse(isle.removeProhibited());
    }

    @Test
    public void testJoin() {
        Isle isle1 = new Isle(0);
        Isle isle2 = new Isle(1);
        isle1.addStudent(Colour.Unicorns);
        isle2.addStudent(Colour.Frogs);
        isle2.addStudent(Colour.Unicorns);
        Isle isle = isle1.join(isle2);
        assertEquals(2,isle.getSize());
        assertEquals(2,isle.getStudents(Colour.Unicorns));
        assertEquals(1,isle.getStudents(Colour.Frogs));
    }

}