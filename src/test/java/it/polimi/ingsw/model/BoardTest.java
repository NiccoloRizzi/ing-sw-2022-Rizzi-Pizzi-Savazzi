package it.polimi.ingsw.model;

import junit.framework.TestCase;

public class BoardTest extends TestCase {

    public void testRemoveStudent() {
        Board board = new Board(Faction.Black, 8);
        Student stud = new Student(Colour.Dragons);
        board.addStudent(stud);
        assertEquals(board.students.size(),1);
        board.removeStudent(0);
        assertEquals(board.students.size(),0);

    }

    public void testRemoveFromTable(){
        Board board = new Board(Faction.Black,8);
        board.addToTable(0);
        assertEquals(1,board.getTable(Colour.values()[0]));
        board.removeFromTable(0);
        assertEquals(0,board.getTable(Colour.values()[0]));
        assertFalse(board.removeFromTable(0));
    }
    public void testAddToEntrance() {
        Board board= new Board(Faction.Black, 8);
        Student stud = new Student(Colour.Dragons);
        board.addToEntrance(stud);
        for(int i=1; i<7;i++){
            board.addToEntrance(new Student(Colour.values()[i%5]));
        }
        assertEquals(7, board.students.size());
        assertFalse(board.addToEntrance(stud));
    }


    public void testAddToTable() {
        Board board = new Board(Faction.Black, 8);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(j, board.getTable(Colour.values()[i]));
                assertTrue(board.addToTable(i));
            }
            assertFalse(board.addToTable(i));
            assertEquals(board.getTable(Colour.values()[i]), 10);
        }
    }

    public void testUseAddTower() {
        Board board = new Board(Faction.Black, 8);
        assertEquals(board.getTowers(),8);
        assertFalse(board.addTower());
        board.useTower();
        assertEquals(board.getTowers(),7);
        assertTrue(board.addTower());
        assertEquals(board.getTowers(),8);
        for(int i=0; i<8;i++)
            board.useTower();
        assertEquals(board.getTowers(),0);
        assertFalse(board.useTower());
    }


    public void testIsTableFull() {
        for(int k=0; k<5; k++) {
            Board board = new Board(Faction.Black, 8);
            for (int i = 0; i < 10; i++) {
                assertFalse(board.isTableFull(k));
                board.addToTable(k);
            }
            assertTrue(board.isTableFull((k)));
        }
    }

    public void testGetTable(){
        Board board = new Board(Faction.Black, 8);
        assertEquals(board.getTable(Colour.values()[0]),0);
    }

    public void testIsEntranceFull() {
        Board board = new Board(Faction.Black, 8);
        assertFalse(board.isEntranceFull());
        Student stud = new Student(Colour.Dragons);
        for(int i=0; i<6;i++){
            board.addToEntrance(stud);
            assertFalse(board.isEntranceFull());
        }
        board.addToEntrance(stud);
        assertTrue(board.isEntranceFull());

        Board board2 = new Board(Faction.Black, 6);
        assertFalse(board2.isEntranceFull());
        for(int i=0; i<8;i++){
            board2.addToEntrance(stud);
            assertFalse(board2.isEntranceFull());
        }
        board2.addToEntrance(stud);
        assertTrue(board2.isEntranceFull());
    }

    public void testGetSetFaction() {
        Board board = new Board(Faction.Black, 8);
        assertEquals(board.getFaction(), Faction.Black);
        board.setFaction(Faction.White);
        assertEquals(board.getFaction(), Faction.White);
    }


    public void testGetTowers() {
        Board board = new Board(Faction.Black, 8);
        assertEquals(board.getTowers(),8);
        board.useTower();
        assertEquals(board.getTowers(),7);
    }

    public void testCheckCoin() {
        Board board = new Board(Faction.Black, 8);
        assertFalse(board.checkCoin(0));
        board.addToTable(0);
        assertFalse(board.checkCoin(0));
        board.addToTable(0);
        assertFalse(board.checkCoin(0));
        board.addToTable(0);
        assertTrue(board.checkCoin(0));
        board.addToTable(0);
        assertFalse(board.checkCoin(0));
    }
}