package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @org.junit.jupiter.api.Test
    void removeStudent() {
        Board board = new Board(Faction.Black, 8);
        board.addStudent(Colour.Dragons);
        assertEquals(board.students.get(Colour.Dragons),1);
        try {
            board.removeStudent(Colour.Dragons);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertEquals(board.getStudents(Colour.Dragons),0);
    }

    @ParameterizedTest
    @ValueSource(ints = {6,8})
    void addToEntrance(int k) {
        Board board= new Board(Faction.Black, k);
        try {
            board.addToEntrance(Colour.Dragons);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        for(int i=1; i<((k==6)?9:7);i++){
            try {
                board.addToEntrance(Colour.Dragons);
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        assertThrowsExactly(StudentsOutOfBoundsException.class, ()->board.addToEntrance(Colour.Dragons));
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
                try{
                    board.addToTable(Colour.values()[i]);
                }catch(StudentsOutOfBoundsException e){
                    e.printStackTrace();
                }

            }
            Colour c = Colour.values()[i];
            assertThrowsExactly(StudentsOutOfBoundsException.class,()->board.addToTable(c));
            assertEquals(board.getTable(Colour.values()[i]), 10);
        }
    }


    @ParameterizedTest
    @ValueSource(ints = {6,8})
    void useAddTowers(int k) {
        Board board = new Board(Faction.Black, k);
        assertEquals(board.getTowers(),k);
        board.useTowers(1);
        assertEquals(board.getTowers(),k-1);
        board.addTowers(1);
        assertEquals(board.getTowers(),k);
        for(int i=0; i<k;i++)
            board.useTowers(1);
        assertEquals(board.getTowers(),0);
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4})
    void isTableFull(int k) {
            Board board = new Board(Faction.Black, 8);
            for (int i = 0; i < 10; i++) {
                assertFalse(board.isTableFull(Colour.values()[k]));
                try {
                    board.addToTable(Colour.values()[k]);
                }catch(StudentsOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
            assertTrue(board.isTableFull((Colour.values()[k])));
    }

    @org.junit.jupiter.api.Test
    void isEntranceFull() {
        Board board = new Board(Faction.Black, 8);
        assertFalse(board.isEntranceFull());
        Colour c= Colour.Dragons;
        for(int i=0; i<6;i++){
            try {
                board.addToEntrance(c);
            }
                catch(StudentsOutOfBoundsException e){
                    e.printStackTrace();
                }
            assertFalse(board.isEntranceFull());
        }
        try {
            board.addToEntrance(c);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertTrue(board.isEntranceFull());

        Board board2 = new Board(Faction.Black, 6);
        assertFalse(board2.isEntranceFull());
        for(int i=0; i<8;i++){
            try {
                board2.addToEntrance(c);
                assertFalse(board2.isEntranceFull());
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }

        }
        try {
            board2.addToEntrance(c);
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
        board.useTowers(1);
        assertEquals(board.getTowers(),param-1);
    }

    @org.junit.jupiter.api.Test
    void removeFromTable() {
        Board board = new Board(Faction.Black,8);
        try {
            board.addToTable(Colour.values()[0]);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertEquals(1,board.getTable(Colour.values()[0]));
        try{
            board.removeFromTable(Colour.values()[0]);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertEquals(0,board.getTable(Colour.values()[0]));
        assertThrowsExactly(StudentsOutOfBoundsException.class ,() -> board.removeFromTable(Colour.values()[0]));
    }


    @ParameterizedTest
    @ValueSource(ints= {0,1,2,3,4})
    void checkCoin(int input){
        try {
            Colour param = Colour.values()[input];
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
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
    }
}