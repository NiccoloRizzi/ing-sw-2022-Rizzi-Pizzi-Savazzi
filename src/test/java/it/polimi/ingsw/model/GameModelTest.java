package it.polimi.ingsw.model;


//import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
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
        GameModel gm = new GameModel(2);
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
            GameModel gm = new GameModel(2);
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
        GameModel gm = new GameModel(2);
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
        GameModel gm = new GameModel(2);
        Colour s = Colour.Dragons;
        try{
            s = gm.getRandomStudent();
        }catch(StudentsOutOfBoundsException e)
        {
            e.printStackTrace();
        }

        assertEquals(gm.getStudents(s),24-1);
    }

    @ParameterizedTest
    @ValueSource(ints={0,1,2,3,4})
    void addStudent(int i) {
        GameModel gm = new GameModel(2);
        int start = gm.getStudents(Colour.values()[i]);
        gm.addStudent(Colour.values()[i]);
        assertEquals(start+1,gm.getStudents(Colour.values()[i]));
    }

    @Test
    void getSetProfessor() {
        GameModel gm = new GameModel(2);
        Player p = new Player(0,"Guybrush");
        gm.setProfessor(Colour.Dragons,p);
        assertTrue(gm.getProfessorOwner(Colour.Dragons).isPresent());
        assertEquals(gm.getProfessorOwner(Colour.Dragons).get(),p);
    }

}