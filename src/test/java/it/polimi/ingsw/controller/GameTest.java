package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.PlayerOutOfBoundException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.exceptions.TowerOutOfBoundException;
import it.polimi.ingsw.messages.AssistantChoiceMessage;
import it.polimi.ingsw.messages.MoveStudentMessage;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;


    void setup(int players, boolean expertMode) throws PlayerOutOfBoundException {
        game = new Game(players,expertMode);
        game.createPlayer("Giorgio");
        game.createPlayer("Fabrizio");
        if(players>2) {
            game.createPlayer("Gennaro");
            if(players>3)
            game.createPlayer("Ciro");
        }
        game.setupGame();
    }

    @Test
    void alreadyUsed() throws PlayerOutOfBoundException {
        setup(4,true);
        Player player = game.getGameModel().getPlayer(game.getCurrentPlayer());
        assertFalse(game.alreadyUsed(0));
        player.setChoosenAssistant(0);
        game.nextPlayer();
        assertTrue(game.alreadyUsed(0));
    }

    @ParameterizedTest
    @ValueSource(ints ={1,2,3,4})
    void getWinner(int typeCheck) throws TowerOutOfBoundException, PlayerOutOfBoundException {
        setup(3,true);
        Player player1 = game.getGameModel().getPlayer(0);
        Player player2 = game.getGameModel().getPlayer(1);
        Player player3 = game.getGameModel().getPlayer(2);
        player1.getBoard().useTowers(1);
        assertEquals(player1,game.getWinner());
        player2.getBoard().useTowers(2);
        assertEquals(player2,game.getWinner());
        player1.getBoard().useTowers(1);
        player3.getBoard().useTowers(2);
        try {
            player3.getBoard().addToTable(Colour.Dragons);
            game.getGameModel().setProfessor(Colour.Dragons,player3);
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }
        assertEquals(player3,game.getWinner());
    }




    @ParameterizedTest
    @ValueSource(ints ={1,2,3,4})
    void checkEnd(int typeCheck) throws TowerOutOfBoundException, PlayerOutOfBoundException {
        setup(4,true);
        Player player = game.getGameModel().getPlayer(0);
        switch(typeCheck) {
            case 1:
                assertFalse(game.checkEnd());
                player.getBoard().useTowers(8);
                assertTrue(game.checkEnd());
                assertEquals(game.getWinner(),player);
                break;

            case 2:
                game.startActionTurn();
                assertFalse(game.checkEnd());
                for(int i=0;i<12;i++){
                    try {
                        game.getGameModel().getIsle(i).setTower((i < 4 || i > 7) ? Faction.Black : Faction.White);
                    }catch(TileOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
                for(int i=0;i<12;i++){
                    game.getTurnHandler().checkIsleJoin(i%game.getGameModel().getIsles().size());
                }
                assertTrue(game.checkEnd());
                break;
            case 3:
                assertFalse(game.checkEnd());
                try {
                    game.getGameModel().extractStudents(game.getGameModel().getBagSize());
                }catch(StudentsOutOfBoundsException e){
                    e.printStackTrace();
                }
                assertTrue(game.checkEnd());
                break;
            case 4:
                assertFalse(game.checkEnd());
                for(int i=0;i<10;i++){
                    assertFalse(game.checkEnd());
                    player.setChoosenAssistant(player.getDeck().size()-1);
                }
                assertTrue(game.checkEnd());
                break;

        }
    }


    @ParameterizedTest
    @ValueSource(ints = {2,3,4})
    void setupGame(int players) throws TileOutOfBoundsException, PlayerOutOfBoundException {
        HashMap<Colour, Integer> students = new HashMap<>();
        for(Colour c: Colour.values()){
            students.put(c,0);
        }
        Game game = new Game(players,true);
        game.createPlayer("Giorgio");
        game.createPlayer("Piero");
        if(players>2){
            game.createPlayer("Alberto");
            if(players>3)
                game.createPlayer("Lorenzo");
        }
        for(Colour c: Colour.values()){
            assertEquals(24, game.getGameModel().getStudents(c));
        }
        game.setupGame();
        for(int i=0; i<12;i++){
            for(Colour c: Colour.values()) {
                students.replace(c,students.get(c)+game.getGameModel().getIsle(i).getStudents(c));
            }
        }
        for(Colour c: Colour.values()){
            assertEquals(students.get(c),2);
        }


    }



    @Test
    @CsvSource({"2,3,ciao"})
    void createPlayer() throws PlayerOutOfBoundException {
        Game game = new Game(2, true);
        game.createPlayer("Giorgio");
        game.createPlayer("Paolo");
        game.setupGame();
        ArrayList<Player> players = game.getGameModel().getPlayers();
        assertEquals(new Player(0,"Giorgio"), players.get(0));
        assertEquals(new Player(1,"Paolo"), players.get(1));
        assertEquals(players.size(),2);
    }

    @ParameterizedTest
    @ValueSource(ints = {2,3,4})
    void getCurrentPlayer(int players) throws PlayerOutOfBoundException {
        setup(players,true);
        for(int i = 0; i<game.getPlanningOrder().size(); i++){
            assertEquals(game.getPlanningOrder().get(i),(game.getCurrentPlayer()+i)%game.getPlanningOrder().size());
        }
    }


    @Test
    void checkNextOrder() throws PlayerOutOfBoundException {
        setup(4,true);
        MessageVisitor mv = new MessageVisitor(game);
        ArrayList<Integer> order = new ArrayList<>(game.getPlanningOrder());
        for(int i=0; i<game.getPlayersNumber();i++){
            AssistantChoiceMessage acm = new AssistantChoiceMessage(i,game.getPlanningOrder().get(i));
            mv.visit(acm);
        }
        assertEquals(game.getActionOrder(),order);


    }

}