package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TowerOutOfBoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class TeamTest {

    @Test
    void getLeader() {
        Team t = new Team();
        t.addPlayer(new Player(0,""));
        t.addPlayer(new Player(1, ""));
        assertNotNull(t.getLeader());
        assertEquals(0,t.getLeader().getID());
    }

    @Test
    void getMember() {
        Team t = new Team();
        t.addPlayer(new Player(0,""));
        t.addPlayer(new Player(1, ""));
        assertNotNull(t.getLeader());
        assertEquals(1,t.getMember().getID());
    }

    @ParameterizedTest
    @EnumSource(Faction.class)
    void getFaction(Faction faction) {
        Team t = new Team();
        Player p1 = new Player(0,"");
        Player p2 = new Player(1, "");
        p1.createBoard(8);
        p2.createBoard(8);
        t.addPlayer(p1);
        t.addPlayer(p2);
        assertEquals(Faction.Empty, t.getFaction());
        t.assignFaction(faction);
        assertEquals(faction, t.getFaction());
    }

    @ParameterizedTest
    @EnumSource(Faction.class)
    void assignFaction(Faction faction) {
        Team t = new Team();
        Player p1 = new Player(0,"");
        Player p2 = new Player(1, "");
        p1.createBoard(8);
        p2.createBoard(8);
        t.addPlayer(p1);
        t.addPlayer(p2);
        assertEquals(Faction.Empty, t.getFaction());
        t.assignFaction(faction);
        assertEquals(faction, t.getFaction());
        assertEquals(faction, t.getLeader().getBoard().getFaction());
        assertEquals(faction, t.getMember().getBoard().getFaction());
    }

    @Test
    void addPlayer()  {
        Team t = new Team();
        assertDoesNotThrow(()->t.addPlayer(new Player(0, "")));
        assertDoesNotThrow(()->t.addPlayer(new Player(1, "")));
        assertThrowsExactly(IndexOutOfBoundsException.class, ()->t.addPlayer(new Player(2, "")));
    }

    @Test
    void useAddTowers() throws TowerOutOfBoundException {
        Team t = new Team();
        Player p1 = new Player(0,"Giorgio");
        Player p2 = new Player(1, "Fabrizio");
        p1.createBoard(8);
        p2.createBoard(8);
        t.addPlayer(p1);
        t.addPlayer(p2);
        t.useTowers(4);
        assertEquals(p1.getBoard().getTowers(), 4);
        assertEquals(p1.getBoard().getTowers(), 4);
        t.addTowers(2);
        assertEquals(p1.getBoard().getTowers(),6);
        assertEquals(p2.getBoard().getTowers(),6);

    }
}