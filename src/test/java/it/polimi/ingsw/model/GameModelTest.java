package it.polimi.ingsw.model;


//import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.clientModels.ClientGameModel;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.server.Observer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest{

    private static class TestObs implements Observer<ClientModel>{
        public ClientModel message;

        @Override
        public void update(ClientModel message) {
            this.message = message;
        }
    }

    @Test
    void extractStudents() {
        GameModel gm = new GameModel(2,true);
        HashMap<Colour, Integer> h= new HashMap<>();
        try {
            h = gm.extractStudents(68);
        }catch(StudentsOutOfBoundsException e)
        {
            e.printStackTrace();
        }
        for (Colour c : Colour.values()) {
            assertEquals(h.get(c), 24 - gm.getStudents(c));
        }
    }


    @Test
    void getPlayer() {
    }

    @Test
    void getIsle() {
    }

    @Test
    void getCloud() {
    }

    @Test
    void getTeam() {
    }


    @Test
    void addCoins() {
        try {
            GameModel gm = new GameModel(2,true);
            gm.removeCoin();
            assertEquals(gm.getCoins(), 19);
            gm.addCoins(1);
            assertEquals(20, gm.getCoins());
        }catch(NotEnoughCoinsException e){
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(ints={-2,-1,0,1,2,3,4,5,6,7,8,9,10,11,12})
    void setGetMoveMN(int param) {
        GameModel gm = new GameModel(2,true);
        if(param<12 && param>=0) {
            assertDoesNotThrow(() -> gm.setMotherNPos(param));
            gm.moveMN(param);
//            ClientGameModel m = (ClientGameModel)obs.message;
            assertEquals(gm.getMotherNature(), (param + param) % 12);
        }
        else{
            assertThrows(TileOutOfBoundsException.class,()->gm.setMotherNPos(param));
        }
    }

    @Test
    void getRandomStudent() {
        GameModel gm = new GameModel(2,true);
        Colour s = Colour.Dragons;
        try{
            s = gm.extractRandomStudent();
        }catch(StudentsOutOfBoundsException e)
        {
            e.printStackTrace();
        }

        assertEquals(gm.getStudents(s),24-1);
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4})
    void addStudent(int i) {
        GameModel gm = new GameModel(2,true);
        int start = gm.getStudents(Colour.values()[i]);
        gm.addStudent(Colour.values()[i]);
        assertEquals(start+1,gm.getStudents(Colour.values()[i]));
    }

    @Test
    void getSetProfessor() {
        GameModel gm = new GameModel(2,true);
        Player p = new Player(0,"Guybrush");
        TestObs obs = new TestObs();
        gm.addObserver(obs);
        gm.setProfessor(Colour.Dragons,p);
        ClientGameModel message = (ClientGameModel) obs.message;
        assertTrue(gm.getProfessorOwner(Colour.Dragons).isPresent());
        assertEquals(gm.getProfessorOwner(Colour.Dragons).get(),p);
        assertEquals(message.getProfessors().get(Colour.Dragons),0);
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4,5,6,7,8,9,10,11})
    void joinIsleTest(int i) {
        GameModel gm = new GameModel(2,true);
        TestObs obs = new TestObs();
        gm.addObserver(obs);
        Isle isle = new Isle(-1);
        try {
            isle = gm.getIsle(i);
        }catch (TileOutOfBoundsException ignore){}
        assertDoesNotThrow(()->gm.getIsle(i).setTower(Faction.Black));
        assertDoesNotThrow(()->gm.getIsle((i==0)?11:i-1).setTower(Faction.Black));
        assertDoesNotThrow(()->gm.getIsle((i+1)%12).setTower(Faction.Black));
        assertDoesNotThrow(()->gm.joinIsle(i));
        ClientGameModel message = (ClientGameModel) obs.message;
        try {
            assertEquals(isle.getID(), gm.getIsle((i==0)?0:i-1).getID());
            assertEquals(3,gm.getIsle((i==0)?0:i-1).getSize());
            assertEquals(10,gm.getIsles().size());
            assertEquals(10,message.getIsles().size());
            assertEquals(isle.getID(),message.getIsles().get((i==0)?0:i-1).getId());

        }catch (TileOutOfBoundsException ignore){}


    }
}