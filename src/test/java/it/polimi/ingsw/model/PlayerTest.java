package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9})
    void getChosen(int param) {
        Player p = new Player(0, "testName");
        assertThrowsExactly(IndexOutOfBoundsException.class, ()->{p.getChosen();});
        p.setChoosenAssistant(param);
        assertNotNull(p.getChosen());
        assertEquals(param+1, p.getChosen().getValue());
        assertEquals((param+2)/2, p.getChosen().getMn_moves());
    }

    @org.junit.jupiter.api.Test
    void getDeck() {
        Player p = new Player(0, "testNickname");
        assertNotNull(p.getDeck());
        assertEquals(10, p.getDeck().size());
        p.setChoosenAssistant(0);
        assertNotNull(p.getDeck());
        assertEquals(9, p.getDeck().size());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,4,5,6,7,8,9,10,11,-1,-2})
    void setChoosenAssistant(int param) {
        Player p = new Player(0, "testNickname");
        if(param >= 0 && param < 10){
            p.setChoosenAssistant(param);
            assertEquals(9, p.getDeck().size());
            assertEquals(1, p.getUsedCards().size());
        }else{
            assertThrowsExactly(IndexOutOfBoundsException.class, ()->{p.setChoosenAssistant(param);});
            assertEquals(10, p.getDeck().size());
            assertEquals(0, p.getUsedCards().size());
        }
    }

    @org.junit.jupiter.api.Test
    void getBoard() {
        Player p = new Player(0, "testNickname");
        assertNull(p.getBoard());
        p.createBoard(8);
        assertNotNull(p.getBoard());
    }

    @org.junit.jupiter.api.Test
    void addCoin() {
        Player p = new Player(0, "testNickname");
        assertEquals(0, p.getCoins());
        p.addCoin();
        assertEquals(1,p.getCoins());
    }

    @org.junit.jupiter.api.Test
    void getCoins(){
        Player p = new Player(0, "testNickName");
        assertEquals(0, p.getCoins());
        p.addCoin();
        assertEquals(1, p.getCoins());
    }

    @ParameterizedTest
    @EnumSource(Faction.class)
    void assignFaction(Faction faction) {
        Player p = new Player(0, "testNickname");
        p.createBoard(8);
        assertEquals(Faction.Empty, p.getBoard().getFaction());
        p.assignFaction(faction);
        assertEquals(faction, p.getBoard().getFaction());
    }

    @org.junit.jupiter.api.Test
    void getUsedCards() {
        Player p = new Player(0, "testNickname");
        assertEquals(0, p.getUsedCards().size());
        p.setChoosenAssistant(0);
        assertNotNull(p.getUsedCards());
        assertEquals(1, p.getUsedCards().size());
    }

    @org.junit.jupiter.api.Test
    void setNickname() {
        Player p = new Player(0, "testNickname");
        assertEquals("testNickname", p.getNickname());
        p.setNickname("testNickName");
        assertNotEquals("testNickname", p.getNickname());
        assertEquals("testNickName", p.getNickname());
    }

    @org.junit.jupiter.api.Test
    void getNickname() {
        Player p = new Player(0, "testNickname");
        assertEquals("testNickname", p.getNickname());
        p.setNickname("testNickName");
        assertNotEquals("testNickname", p.getNickname());
        assertEquals("testNickName", p.getNickname());
    }

    @org.junit.jupiter.api.Test
    void getID() {
        Player p = new Player(0, "testNickname");
        assertEquals(0, p.getID());
    }

    @ParameterizedTest
    @ValueSource(ints = {6,8})
    void createBoard(int param) {
        Player p = new Player(0, "testNickname");
        assertNull(p.getBoard());
        p.createBoard(param);
        assertNotNull(p.getBoard());
        assertEquals(param, p.getBoard().getTowers());
    }

    @Test
    void testHashCode() {
        Player p = new Player(0, "");
        Player p2 = new Player(0, "");
        Player p3 = new Player(1, "");
        assertEquals(p.hashCode(), p2.hashCode());
        assertNotEquals(p2.hashCode(), p3.hashCode());
    }
}