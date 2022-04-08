package it.polimi.ingsw.model;


//import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {


    @Test
    void extractStudents() {
        GameModel gm =new GameModel();
        HashMap <Colour, Integer> h = gm.extractStudents(4);
        for(Colour c: Colour.values()){
            assertEquals(h.get(c),24-gm.getStudents(c));
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
            GameModel gm = new GameModel();
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
        GameModel gm = new GameModel();
        if(param<12 && param>=0) {
            assertDoesNotThrow(() -> gm.setMotherNPos(param));
            gm.moveMN(param);
            assertEquals(gm.getMotherNature(), (param + param) % 12);
        }
        else{
            assertThrows(TileOutOfBoundsException.class,()->gm.setMotherNPos(param));
        }
    }

    @Test
    void getRandomStudent() {
        GameModel gm = new GameModel();
        Colour s = gm.getRandomStudent();
        assertEquals(gm.getStudents(s),24-1);
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4})
    void addStudent(int i) {
        GameModel gm = new GameModel();
        int start = gm.getStudents(Colour.values()[i]);
        gm.addStudent(Colour.values()[i]);
        assertEquals(start+1,gm.getStudents(Colour.values()[i]));
    }

    @Test
    void getSetProfessor() {
        GameModel gm = new GameModel();
        Player p = new Player(0,"Guybrush");
        gm.setProfessor(Colour.Dragons,p);
        assertEquals(gm.getProfessor(Colour.Dragons),p);
    }

}