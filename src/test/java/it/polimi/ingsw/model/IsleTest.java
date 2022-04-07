package it.polimi.ingsw.model;

import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

public class IsleTest extends TestCase {

    @Test
    public void testTower() {
        Isle isle = new Isle();
        assertEquals(Faction.Empty,isle.getTower());
        isle.setTower(Faction.Black);
        assertEquals(Faction.Black,isle.getTower());
    }

    @Test
    public void testGetInfluenceDefault() {
        Isle isle = new Isle();
        isle.setTower(Faction.Black);
        ArrayList<Student> s = new ArrayList<Student>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
            for(int i = 0; i < numStudent[c.ordinal()];i++)
                s.add(new Student(c));
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<Colour,Player>();
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
        Isle isle = new Isle();
        isle.setInfStrategy(new noTowersStrategy());
        isle.setTower(Faction.Black);
        ArrayList<Student> s = new ArrayList<Student>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
            for(int i = 0; i < numStudent[c.ordinal()];i++)
                s.add(new Student(c));
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<Colour,Player>();
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
        Isle isle = new Isle();
        isle.setInfStrategy(new PlusInfStrategy());
        isle.setTower(Faction.Black);
        ArrayList<Student> s = new ArrayList<Student>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
            for(int i = 0; i < numStudent[c.ordinal()];i++)
                s.add(new Student(c));
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<Colour,Player>();
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
        assertEquals(numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()]+3,isle.getInfluence(p1,professors));
        assertEquals(numStudent[Colour.Frogs.ordinal()]+numStudent[Colour.Fairies.ordinal()]+2,isle.getInfluence(p2,professors));
        assertEquals(numStudent[Colour.Unicorns.ordinal()]+numStudent[Colour.Gnomes.ordinal()]+numStudent[Colour.Dragons.ordinal()]+3,isle.getInfluence(t,professors));
    }

    @Test
    public void testGetInfluenceNoColour() {
        Isle isle = new Isle();
        isle.setTower(Faction.Black);
        isle.setInfStrategy(new NoColourStrategy(Colour.Dragons));
        ArrayList<Student> s = new ArrayList<Student>();
        int[] numStudent = {1,2,3,4,5};
        for(Colour c:Colour.values())
            for(int i = 0; i < numStudent[c.ordinal()];i++)
                s.add(new Student(c));
        isle.addStudents(s);
        HashMap<Colour,Player> professors = new HashMap<Colour,Player>();
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
        Isle isle = new Isle();
        assertFalse(isle.prohibited);
        isle.setProhibited();
        assertTrue(isle.prohibited);
        isle.removeProhibited();
        assertFalse(isle.prohibited);
    }

    @Test
    public void testJoin() {
        Isle isle1 = new Isle();
        Isle isle2 = new Isle();
        AggregatedIsland isle = isle1.join(isle2);
        assertTrue(isle.getJoinedIsle().contains(isle1));
        assertTrue(isle.getJoinedIsle().contains(isle2));
    }


    @Test
    public void testTestJoin() {
        Isle isle1 = new Isle();
        Isle isle2 = new Isle();
        Isle isle3 = new Isle();
        AggregatedIsland isle = isle1.join(isle2);
        isle = isle3.join(isle);
        assertTrue(isle.getJoinedIsle().contains(isle1));
        assertTrue(isle.getJoinedIsle().contains(isle2));
        assertTrue(isle.getJoinedIsle().contains(isle3));
    }
}