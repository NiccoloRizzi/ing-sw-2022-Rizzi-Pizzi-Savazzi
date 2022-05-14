package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientBoard;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TowerOutOfBoundException;
import it.polimi.ingsw.server.Observer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private class TestObs implements Observer<ClientModel> {
        public ClientModel message;

        @Override
        public void update(ClientModel message) {
            this.message = message;
        }
    }

    @org.junit.jupiter.api.Test
    void removeStudent() {
        Board board = new Board(Faction.Black, 8,0);
        TestObs obs = new TestObs();
        board.addObserver(obs);
        board.addStudent(Colour.Dragons);
        ClientBoard message = (ClientBoard)obs.message;
        assertEquals(board.students.get(Colour.Dragons),1);
        assertEquals(message.getEntrance().get(Colour.Dragons),1);
        try {
            board.removeStudent(Colour.Dragons);
            message = (ClientBoard)obs.message;
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertEquals(board.getStudents(Colour.Dragons),0);
        assertEquals(message.getEntrance().get(Colour.Dragons),0);
    }

    @ParameterizedTest
    @ValueSource(ints = {6,8})
    void addToEntrance(int k) {
        Board board= new Board(Faction.Black, k,0);
        TestObs obs = new TestObs();
        board.addObserver(obs);
        ClientBoard message;
        try {
            board.addToEntrance(Colour.Dragons);
            message = (ClientBoard)obs.message;
            assertEquals(1,message.getEntrance().get(Colour.Dragons));
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        for(int i=1; i<((k==6)?9:7);i++){
            try {
                board.addToEntrance(Colour.Dragons);
                message = (ClientBoard)obs.message;
                assertEquals(i+1,message.getEntrance().get(Colour.Dragons));
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        assertThrowsExactly(StudentsOutOfBoundsException.class, ()->board.addToEntrance(Colour.Dragons));
    }

    @org.junit.jupiter.api.Test
    void getTable() {
        Board board = new Board(Faction.Black, 8,0);
        assertEquals(board.getTable(Colour.values()[0]),0);
    }

    @org.junit.jupiter.api.Test
    void addToTable() {
        Board board = new Board(Faction.Black, 8,0);
        TestObs obs = new TestObs();
        board.addObserver(obs);
        ClientBoard message;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(j, board.getTable(Colour.values()[i]));
                try{
                    board.addToTable(Colour.values()[i]);
                    message = (ClientBoard)obs.message;
                    assertEquals(j+1, message.getTables().get(Colour.values()[i]));
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
    void useAddTowers(int k) throws TowerOutOfBoundException {
        Board board = new Board(Faction.Black, k,0);
        TestObs obs = new TestObs();
        board.addObserver(obs);
        ClientBoard message;
        assertEquals(board.getTowers(),k);
        board.useTowers(1);
        message = (ClientBoard)obs.message;
        assertEquals(k-1,message.getTowers());
        assertEquals(board.getTowers(),k-1);
        board.addTowers(1);
        message = (ClientBoard)obs.message;
        assertEquals(k,message.getTowers());
        assertEquals(board.getTowers(),k);
        for(int i=0; i<k;i++)
            board.useTowers(1);
        assertEquals(board.getTowers(),0);
        message = (ClientBoard)obs.message;
        assertEquals(0,message.getTowers());
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4})
    void isTableFull(int k) {
            Board board = new Board(Faction.Black, 8,0);
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
        Board board = new Board(Faction.Black, 8,0);
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

        Board board2 = new Board(Faction.Black, 6,0);
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
        Board board = new Board(Faction.Black, 8,0);
        TestObs obs = new TestObs();
        board.addObserver(obs);
        ClientBoard message;
        assertEquals(board.getFaction(), Faction.Black);
        board.setFaction(Faction.White);
        message = (ClientBoard)obs.message;
        assertEquals(board.getFaction(), Faction.White);
        assertEquals(message.getFaction(),Faction.White);
    }

    @ParameterizedTest
    @ValueSource(ints ={6,8})
    void getTowers(int param) throws TowerOutOfBoundException {
        Board board = new Board(Faction.Black, param,0);
        assertEquals(board.getTowers(),param);
        board.useTowers(1);
        assertEquals(board.getTowers(),param-1);
    }

    @org.junit.jupiter.api.Test
    void removeFromTable() {
        Board board = new Board(Faction.Black,8,0);
        TestObs obs = new TestObs();
        board.addObserver(obs);
        ClientBoard message;
        try {
            board.addToTable(Colour.values()[0]);
            message = (ClientBoard) obs.message;
            assertEquals(1,message.getTables().get(Colour.values()[0]));
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertEquals(1,board.getTable(Colour.values()[0]));
        try{
            board.removeFromTable(Colour.values()[0]);
            message = (ClientBoard) obs.message;
            assertEquals(0,message.getTables().get(Colour.values()[0]));
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
            Board board = new Board(Faction.Black, 8,0);
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