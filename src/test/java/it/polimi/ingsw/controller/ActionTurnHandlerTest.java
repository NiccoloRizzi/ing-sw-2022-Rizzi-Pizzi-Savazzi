package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.AggregatedIsland;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Isle;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ActionTurnHandlerTest {

    @Test
    void moveMn() {
        Game game = new Game(2,false);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setProfessor(Colour.Dragons,game.getGameModel().getPlayer(0));
        game.getGameModel().setProfessor(Colour.Gnomes,game.getGameModel().getPlayer(1));
        int player = game.getCurrentPlayer();
        int initpos = game.getGameModel().getMotherNature();
        try {
            game.getGameModel().getIsle((initpos + 3) % 12).addStudent(Colour.Dragons);
            game.getGameModel().getIsle((initpos + 3) % 12).addStudent(Colour.Dragons);
            game.getGameModel().getIsle((initpos + 3) % 12).addStudent(Colour.Dragons);
            game.getGameModel().getIsle((initpos + 6) % 12).addStudent(Colour.Gnomes);
            game.getGameModel().getIsle((initpos + 6) % 12).addStudent(Colour.Gnomes);
            game.getGameModel().getIsle((initpos + 6) % 12).addStudent(Colour.Gnomes);
            game.getGameModel().getPlayer(player).setChoosenAssistant(5);
            game.getTurnHandler().moveMn(3);
            assertEquals((initpos+3)%12,game.getGameModel().getMotherNature());
            assertEquals(Faction.Black,game.getGameModel().getIsle((initpos+3)%12).getTower());
            game.getTurnHandler().moveMn(3);
            assertEquals((initpos+6)%12,game.getGameModel().getMotherNature());
            assertEquals(Faction.White,game.getGameModel().getIsle((initpos+6)%12).getTower());
        }catch(TileOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }
    @Test
    void moveMn4() {
        Game game = new Game(4, false);
        game.createPlayer("A");
        game.createPlayer("B");
        game.createPlayer("C");
        game.createPlayer("D");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setProfessor(Colour.Dragons, game.getGameModel().getPlayer(0));
        game.getGameModel().setProfessor(Colour.Gnomes, game.getGameModel().getPlayer(2));
        int player = game.getCurrentPlayer();
        int initpos = game.getGameModel().getMotherNature();
        try {
            game.getGameModel().getIsle((initpos + 3) % 12).addStudent(Colour.Dragons);
            game.getGameModel().getIsle((initpos + 3) % 12).addStudent(Colour.Dragons);
            game.getGameModel().getIsle((initpos + 3) % 12).addStudent(Colour.Dragons);
            game.getGameModel().getIsle((initpos + 6) % 12).addStudent(Colour.Gnomes);
            game.getGameModel().getIsle((initpos + 6) % 12).addStudent(Colour.Gnomes);
            game.getGameModel().getIsle((initpos + 6) % 12).addStudent(Colour.Gnomes);
            game.getGameModel().getPlayer(player).setChoosenAssistant(5);
            game.getTurnHandler().moveMn(3);
            assertEquals((initpos + 3) % 12, game.getGameModel().getMotherNature());
            assertEquals(Faction.Black, game.getGameModel().getIsle((initpos + 3) % 12).getTower());
            game.getTurnHandler().moveMn(3);
            assertEquals((initpos + 6) % 12, game.getGameModel().getMotherNature());
            assertEquals(Faction.White, game.getGameModel().getIsle((initpos + 6) % 12).getTower());
        } catch (TileOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Test
    void moveStudentToIsle() {
        Game game = new Game(2,false);
        Random rand = new Random();
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        int islestud=0;
        int isle = rand.nextInt(game.getGameModel().getIsles().size());
        try {
            islestud = game.getGameModel().getIsle(isle).getStudents(Colour.Dragons);
        }catch(TileOutOfBoundsException e) {
            e.printStackTrace();
        }
        int boardStud = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(Colour.Dragons);

        game.getTurnHandler().moveStudentToIsle(Colour.Dragons,isle);
        if(boardStud != 0) {
            try {
                assertEquals(islestud + 1, game.getGameModel().getIsle(isle).getStudents(Colour.Dragons));
            } catch (TileOutOfBoundsException e) {
                e.printStackTrace();
            }
            assertEquals(boardStud - 1, game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(Colour.Dragons));
        }
        else
        {
            try {
                assertEquals(islestud, game.getGameModel().getIsle(isle).getStudents(Colour.Dragons));
            } catch (TileOutOfBoundsException e) {
                e.printStackTrace();
            }
            assertEquals(boardStud, game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(Colour.Dragons));
        }
    }

    @Test
    void moveStudentToTable() {
        Game game = new Game(2, false);
        Random rand = new Random();
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        int tablestud = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getTable(Colour.Dragons);
        int boardStud = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(Colour.Dragons);

        game.getTurnHandler().moveStudentToTable(Colour.Dragons);
        if(boardStud != 0) {
            assertEquals(tablestud + 1, game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getTable(Colour.Dragons));
            assertEquals(boardStud - 1, game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(Colour.Dragons));
        }
        else {
            assertEquals(tablestud, game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getTable(Colour.Dragons));
            assertEquals(boardStud, game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(Colour.Dragons));
        }
    }

    @Test
    void moveFromCloud() {
        Game game = new Game(2, false);
        Random rand = new Random();
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        HashMap<Colour, Integer> student = new HashMap<>();
        int cloudId = new Random().nextInt(game.getGameModel().getClouds().size());
        for (Colour c : Colour.values()) {
            try {
                student.put(c, game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(c) + game.getGameModel().getCloud(cloudId).getStudents(c));
            } catch (TileOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        game.getTurnHandler().moveFromCloud(cloudId);
        for (Colour c : Colour.values()) {
            assertEquals(student.get(c), game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard().getStudents(c));
        }
    }

    @Test
    void checkIsleJoin() {
        Game game = new Game(2, false);
        int rand = new Random().nextInt(12);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();

        try {
            Isle isle1 = game.getGameModel().getIsle((rand==0)?11:rand-1);
            Isle isle2 = game.getGameModel().getIsle(rand);
            Isle isle3 = game.getGameModel().getIsle((rand+1)%12);

            isle1.setTower(Faction.Black);
            isle2.setTower(Faction.Black);
            isle3.setTower(Faction.Black);

            game.getTurnHandler().checkIsleJoin(rand);

            assertEquals(3,game.getGameModel().getIsle((rand== 0)?0:rand-1).getSize());
            assertEquals(isle1,((AggregatedIsland)(game.getGameModel().getIsle((rand== 0)?0:rand-1))).getJoinedIsle().get(0));
            assertEquals(isle2,((AggregatedIsland)(game.getGameModel().getIsle((rand== 0)?0:rand-1))).getJoinedIsle().get(1));
            assertEquals(isle3,((AggregatedIsland)(game.getGameModel().getIsle((rand== 0)?0:rand-1))).getJoinedIsle().get(2));
        }catch(TileOutOfBoundsException e) {
            e.printStackTrace();
        }
            assertEquals(10,game.getGameModel().getIsles().size());
    }

    @Test
    void getStudentsToMove() {
        Game game = new Game(2, false);
        int rand = new Random().nextInt(12);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        int player = game.getCurrentPlayer();
        assertEquals(3,game.getTurnHandler().getStudentsToMove());
        game.getGameModel().getPlayer(player).getBoard().addStudent(Colour.Dragons);
        game.getGameModel().getPlayer(player).getBoard().addStudent(Colour.Gnomes);
        game.getTurnHandler().moveStudentToTable(Colour.Dragons);
        assertEquals(2,game.getTurnHandler().getStudentsToMove());
        game.getTurnHandler().moveStudentToIsle(Colour.Gnomes,0);
        assertEquals(1,game.getTurnHandler().getStudentsToMove());
    }

    @Test
    void getPhase() {
        Game game = new Game(2, false);
        int rand = new Random().nextInt(12);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        int player = game.getCurrentPlayer();
        game.getGameModel().getPlayer(player).setChoosenAssistant(5);
        game.getGameModel().getPlayer(player).getBoard().addStudent(Colour.Dragons);
        game.getGameModel().getPlayer(player).getBoard().addStudent(Colour.Gnomes);
        assertEquals(Phase.STUDENTS,game.getTurnHandler().getPhase());
        game.getTurnHandler().moveStudentToTable(Colour.Dragons);
        assertEquals(Phase.STUDENTS,game.getTurnHandler().getPhase());
        game.getTurnHandler().moveStudentToIsle(Colour.Gnomes,0);
        assertEquals(Phase.STUDENTS,game.getTurnHandler().getPhase());
        game.getGameModel().getPlayer(player).getBoard().addStudent(Colour.Gnomes);
        game.getTurnHandler().moveStudentToTable(Colour.Gnomes);
        assertEquals(Phase.MOTHERNATURE,game.getTurnHandler().getPhase());
        game.getTurnHandler().moveMn(2);
        assertEquals(Phase.CLOUD,game.getTurnHandler().getPhase());
    }

}