package it.polimi.ingsw.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @org.junit.jupiter.api.Test
    void removeStudent() {
        Board board = new Board(Faction.Black, 8);
        Student stud = new Student(Colour.Dragons);
        board.addStudent(stud);
        assertEquals(board.students.size(),1);
        board.removeStudent(0);
        assertEquals(board.students.size(),0);
    }

    @ParameterizedTest
    @ValueSource(ints = {6,8})
    void addToEntrance(int k) {
        Board board= new Board(Faction.Black, k);
        Student stud = new Student(Colour.Dragons);
        try {
            board.addToEntrance(stud);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        for(int i=1; i<((k==6)?9:7);i++){
            try {
                board.addToEntrance(new Student(Colour.values()[i % 5]));
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        assertEquals((k==6)?9:7, board.students.size());
        try {
            assertFalse(board.addToEntrance(stud));
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void getTable() {
        Board board = new Board(Faction.Black, 8);
        assertEquals(board.getTable(Colour.values()[0]),0);
    }

    @org.junit.jupiter.api.Test
    void addToTable() {
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


    @ParameterizedTest
    @ValueSource(ints = {6,8})
    void useAddTower(int k) {
        Board board = new Board(Faction.Black, k);
        assertEquals(board.getTowers(),k);
        assertFalse(board.addTower());
        board.useTower();
        assertEquals(board.getTowers(),k-1);
        assertTrue(board.addTower());
        assertEquals(board.getTowers(),k);
        for(int i=0; i<k;i++)
            board.useTower();
        assertEquals(board.getTowers(),0);
        assertFalse(board.useTower());
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4})
    void isTableFull(int k) {
            Board board = new Board(Faction.Black, 8);
            for (int i = 0; i < 10; i++) {
                assertFalse(board.isTableFull(k));
                board.addToTable(k);
            }
            assertTrue(board.isTableFull((k)));
    }

    @org.junit.jupiter.api.Test
    void isEntranceFull() {
        Board board = new Board(Faction.Black, 8);
        assertFalse(board.isEntranceFull());
        Student stud = new Student(Colour.Dragons);
        for(int i=0; i<6;i++){
            try {
                board.addToEntrance(stud);
            }
                catch(StudentsOutOfBoundsException e){
                    e.printStackTrace();
                }
            assertFalse(board.isEntranceFull());
        }
        try {
            board.addToEntrance(stud);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertTrue(board.isEntranceFull());

        Board board2 = new Board(Faction.Black, 6);
        assertFalse(board2.isEntranceFull());
        for(int i=0; i<8;i++){
            try {
                board2.addToEntrance(stud);
                assertFalse(board2.isEntranceFull());
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }

        }
        try {
            board2.addToEntrance(stud);
            assertTrue(board2.isEntranceFull());
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }

    }

    @org.junit.jupiter.api.Test
    void getSetFaction() {
        Board board = new Board(Faction.Black, 8);
        assertEquals(board.getFaction(), Faction.Black);
        board.setFaction(Faction.White);
        assertEquals(board.getFaction(), Faction.White);
    }

    @ParameterizedTest
    @ValueSource(ints ={6,8})
    void getTowers(int param) {
        Board board = new Board(Faction.Black, param);
        assertEquals(board.getTowers(),param);
        board.useTower();
        assertEquals(board.getTowers(),param-1);
    }

    @org.junit.jupiter.api.Test
    void removeFromTable() {
        Board board = new Board(Faction.Black,8);
        board.addToTable(0);
        assertEquals(1,board.getTable(Colour.values()[0]));
        board.removeFromTable(0);
        assertEquals(0,board.getTable(Colour.values()[0]));
        assertFalse(board.removeFromTable(0));
    }


    @ParameterizedTest
    @ValueSource(ints= {0,1,2,3,4})
    void checkCoin(int param) {
        Board board = new Board(Faction.Black, 8);
        assertFalse(board.checkCoin(param));
        board.addToTable(param);
        assertFalse(board.checkCoin(param));
        board.addToTable(param);
        assertFalse(board.checkCoin(param));
        board.addToTable(param);
        assertTrue(board.checkCoin(param));
        board.addToTable(param);
        assertFalse(board.checkCoin(param));
    }
}