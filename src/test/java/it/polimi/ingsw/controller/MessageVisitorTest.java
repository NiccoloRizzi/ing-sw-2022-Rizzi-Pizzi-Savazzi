package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MessageVisitorTest {

    private static Stream<Arguments> argsA(){
        return Stream.of(
                Arguments.of(CharactersEnum.NO_TOWER_INFLUENCE,2),
                Arguments.of(CharactersEnum.NO_TOWER_INFLUENCE,3),
                Arguments.of(CharactersEnum.NO_TOWER_INFLUENCE,4),
                Arguments.of(CharactersEnum.PLUS_2_INFLUENCE,2),
                Arguments.of(CharactersEnum.PLUS_2_INFLUENCE,3),
                Arguments.of(CharactersEnum.PLUS_2_INFLUENCE,4),
                Arguments.of(CharactersEnum.NO_COLOUR_INFLUENCE,2),
                Arguments.of(CharactersEnum.NO_COLOUR_INFLUENCE,3),
                Arguments.of(CharactersEnum.NO_COLOUR_INFLUENCE,4)
        );
    }
    private static Stream<Arguments> argsB(){
        return Stream.of(
                Arguments.of(Colour.Dragons, CharactersEnum.ONE_STUD_TO_ISLE),
                Arguments.of(Colour.Dragons, CharactersEnum.ONE_STUD_TO_TABLES),
                Arguments.of(Colour.Fairies,CharactersEnum.ONE_STUD_TO_ISLE),
                Arguments.of(Colour.Fairies, CharactersEnum.ONE_STUD_TO_TABLES),
                Arguments.of(Colour.Frogs,CharactersEnum.ONE_STUD_TO_ISLE),
                Arguments.of(Colour.Frogs,  CharactersEnum.ONE_STUD_TO_TABLES)
        );
    }
    private static Stream<Arguments> argsC(){
        return Stream.of(
                Arguments.of(Colour.Dragons),
                Arguments.of(Colour.Fairies),
                Arguments.of(Colour.Frogs),
                Arguments.of(Colour.Gnomes),
                Arguments.of(Colour.Unicorns)
        );
    }

    @ParameterizedTest
    @MethodSource("argsA")
    void testVisitIsleInfluenceCharacterMessage(CharactersEnum charType, int playerNum) throws TileOutOfBoundsException {

        // Test observes the influence based on this isle, character, player (or team), faction and colors
        final int CHAR_ID = 0;
        final int ISLE_ID = 0;
        final int PLAYER_ID_A = 0;
        final int PLAYER_ID_B = 1;
        final int TEAM_ID = 0;
        Colour COLOUR_A = Colour.Dragons;
        Colour COLOUR_B = Colour.Fairies;
        Faction FACTION = Faction.Black;

        // Init
        Game game = new Game(playerNum, true);
        game.getGameModel().setCharacter_DEBUG(0, charType);
        GameModel gameModel = game.getGameModel();
        for(int p = 0; p < playerNum; p++){
            game.createPlayer("p" + p);
        }

        // Usefully variables
        Isle isle = gameModel.getIsle(ISLE_ID);
        Player player_a = gameModel.getPlayer(PLAYER_ID_A);
        Player player_b = gameModel.getPlayer(PLAYER_ID_B);
        HashMap<Colour, Player> professors = gameModel.getProfessors();

        // Setup test background
        player_a.createBoard((playerNum == 3)?6:8);
        player_b.createBoard((playerNum == 3)?6:8);
        player_a.assignFaction(FACTION);
        if(playerNum == 4){
            gameModel.getTeam(TEAM_ID).assignFaction(FACTION);
        }
        isle.setTower(FACTION);
        isle.addStudent(COLOUR_A);
        isle.addStudent(COLOUR_B);
        gameModel.setProfessor(COLOUR_A, player_a);
        if(playerNum == 4){
            gameModel.setProfessor(COLOUR_B, player_b);
        }

        // Before message condition
        assertEquals(2, isle.getInfluence(player_a, professors));
        if(playerNum == 4){
            assertEquals(3, isle.getInfluence(gameModel.getTeam(0), professors));
        }

        // Test
        IsleInfluenceCharacterMessage message = new IsleInfluenceCharacterMessage(CHAR_ID,PLAYER_ID_A,ISLE_ID, COLOUR_A);
        MessageVisitor visitor = new MessageVisitor(game);
        message.accept(visitor);
        if(charType == CharactersEnum.PLUS_2_INFLUENCE){
            assertEquals(4, isle.getInfluence(player_a, professors));
            if(playerNum == 4){
                assertEquals(5, isle.getInfluence(gameModel.getTeam(TEAM_ID), professors));
            }
        }
        if(charType == CharactersEnum.NO_TOWER_INFLUENCE){
            assertEquals(1, isle.getInfluence(player_a, professors));
            if(playerNum == 1){
                assertEquals(2, isle.getInfluence(gameModel.getTeam(TEAM_ID), professors));
            }

        }
        if(charType == CharactersEnum.NO_COLOUR_INFLUENCE){
            assertEquals(1, isle.getInfluence(player_a, professors));
            if(playerNum == 4){
                assertEquals(2, isle.getInfluence(gameModel.getTeam(TEAM_ID), professors));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("argsB")
    void testVisitMoveStudentCharacterMessage(Colour colour, CharactersEnum charType) throws TileOutOfBoundsException {

        // Test observes the behaviour based on these variables
        final int CHAR_ID = 0;
        final int PLAYER_NUM = 2;
        final int PLAYER_ID = 0;
        final int ISLE_ID = 0;

        // Init
        Game game = new Game(PLAYER_NUM, true);
        game.getGameModel().setCharacter_DEBUG(0, charType);
        GameModel gameModel = game.getGameModel();
        game.createPlayer("p" + PLAYER_ID);
        game.startActionTurn();

        // Usefully variables
        Player player = gameModel.getPlayer(PLAYER_ID);
        CharacterStudents character = (CharacterStudents) gameModel.getCharacter(CHAR_ID);
        Isle isle = gameModel.getIsle(ISLE_ID);

        // Setup test background
        player.createBoard(8);
        character.addStudent(Colour.Dragons);
        character.addStudent(Colour.Dragons);
        character.addStudent(Colour.Fairies);
        character.addStudent(Colour.Frogs);

        // Before message condition
        assertEquals(120, gameModel.getBagSize());
        assertEquals(2, character.getStudents(Colour.Dragons));
        assertEquals(1, character.getStudents(Colour.Fairies));
        assertEquals(1, character.getStudents(Colour.Frogs));
        assertEquals(0, player.getBoard().getTable(Colour.Dragons));
        assertEquals(0, player.getBoard().getTable(Colour.Frogs));
        assertEquals(0, player.getBoard().getTable(Colour.Fairies));
        assertEquals(0, isle.getStudents(Colour.Fairies));
        assertEquals(0, isle.getStudents(Colour.Dragons));
        assertEquals(0, isle.getStudents(Colour.Frogs));

        // Test
        MoveStudentCharacterMessage message = new MoveStudentCharacterMessage(PLAYER_ID, CHAR_ID, colour, ISLE_ID);
        MessageVisitor visitor = new MessageVisitor(game);
        message.accept(visitor);
        if(charType == CharactersEnum.ONE_STUD_TO_TABLES){
            assertEquals(119, gameModel.getBagSize());
            assertEquals((colour==Colour.Dragons)?1:0, player.getBoard().getTable(Colour.Dragons));
            assertEquals((colour==Colour.Frogs)?1:0, player.getBoard().getTable(Colour.Frogs));
            assertEquals((colour==Colour.Fairies)?1:0, player.getBoard().getTable(Colour.Fairies));
            assertEquals(0, isle.getStudents(Colour.Fairies));
            assertEquals(0, isle.getStudents(Colour.Dragons));
            assertEquals(0, isle.getStudents(Colour.Frogs));
        }
        if(charType == CharactersEnum.ONE_STUD_TO_ISLE){
            assertEquals(119, gameModel.getBagSize());
            assertEquals(0, player.getBoard().getTable(Colour.Dragons));
            assertEquals(0, player.getBoard().getTable(Colour.Frogs));
            assertEquals(0, player.getBoard().getTable(Colour.Fairies));
            assertEquals((colour==Colour.Fairies)?1:0, isle.getStudents(Colour.Fairies));
            assertEquals((colour==Colour.Dragons)?1:0, isle.getStudents(Colour.Dragons));
            assertEquals((colour==Colour.Frogs)?1:0, isle.getStudents(Colour.Frogs));
        }
    }

    @ParameterizedTest
    @MethodSource("argsC")
    void testVisitStrategyProfessorMessage(Colour colour) throws StudentsOutOfBoundsException {

        // Referent constants
        final int PLAYER_ID_A = 0;
        final int PLAYER_ID_B = 1;
        final int PLAYERS_NUM = 2;
        final int TOWERS = 6;
        final int CHAR_ID = 0;

        // Init
        Game game = new Game(PLAYERS_NUM, true);
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.PROFESSOR_CONTROL);
        game.createPlayer("p1");
        game.createPlayer("p2");
        game.setupGame();
        game.startActionTurn();
        ActionTurnHandler turn = game.getTurnHandler();
        GameModel gameModel = game.getGameModel();
        Player playerA = gameModel.getPlayer(PLAYER_ID_A);
        Player playerB = gameModel.getPlayer(PLAYER_ID_B);
        playerA.createBoard(TOWERS);
        playerB.createBoard(TOWERS);
        Board boardA = playerA.getBoard();
        Board boardB = playerB.getBoard();

        // Before test condition
        boardA.addToTable(colour);
        turn.setCurrentPlayer(playerA);
        game.setCurrentPlayer(playerA.getID());
        turn.checkProfessor(colour);
        assertEquals(playerA, gameModel.getProfessorOwner(colour).get());
        boardB.addToTable(colour);
        turn.setCurrentPlayer(playerB);
        game.setCurrentPlayer(playerB.getID());
        turn.checkProfessor(colour);
        assertEquals(playerA, gameModel.getProfessorOwner(colour).get());
        boardB.addToTable(colour);
        turn.checkProfessor(colour);
        assertEquals(playerB, gameModel.getProfessorOwner(colour).get());

        // Test
        MessageVisitor visitor = new MessageVisitor(game);
        turn.setCurrentPlayer(playerA);
        game.setCurrentPlayer(playerA.getID());
        StrategyProfessorMessage message = new StrategyProfessorMessage(CHAR_ID, PLAYER_ID_A);
        message.accept(visitor);
        boardA.addToTable(colour);
        turn.checkProfessor(colour);
        assertEquals(playerA, gameModel.getProfessorOwner(colour).get());
        boardB.addToTable(colour);
        turn.setCurrentPlayer(playerB);
        game.setCurrentPlayer(playerB.getID());
        turn.checkProfessor(colour);
        assertEquals(playerB, gameModel.getProfessorOwner(colour).get());
    }

    @Test
    void testVisitSimilMotherNatureMesage() throws TileOutOfBoundsException {

        // Referent constants
        final int PLAYER_ID_A = 0;
        final int PLAYER_ID_B = 1;
        final int ISLE_ID = 0;
        final int PLAYERS_NUM = 2;
        final int TOWERS = 6;
        final int CHAR_ID = 0;

        // Init
        Game game = new Game(PLAYERS_NUM, true);
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.SIMIL_MN);
        game.createPlayer("p1");
        game.createPlayer("p2");
        game.startActionTurn();
        GameModel gameModel = game.getGameModel();
        Player playerA = gameModel.getPlayer(PLAYER_ID_A);
        Player playerB = gameModel.getPlayer(PLAYER_ID_B);
        playerA.createBoard(TOWERS);
        playerB.createBoard(TOWERS);
        Isle isle = gameModel.getIsle(ISLE_ID);
        Isle isleP = gameModel.getIsle((ISLE_ID + 11) % 12);
        Isle isleS = gameModel.getIsle((ISLE_ID + 1) % 12);

        // Setting background
        playerA.assignFaction(Faction.Black);
        playerB.assignFaction(Faction.White);
        gameModel.setProfessor(Colour.Fairies, playerA);
        isle.addStudent(Colour.Dragons);
        isleP.setTower(Faction.Black);
        isleS.setTower(Faction.Black);
        assertEquals(12, gameModel.getIsles().size());
        assertEquals(Faction.Empty, isle.getTower());

        // Test
        MessageVisitor visitor = new MessageVisitor(game);
        SimilMotherNatureMesage message = new SimilMotherNatureMesage(CHAR_ID, PLAYER_ID_A, ISLE_ID);
        message.accept(visitor);
        assertEquals(Faction.Empty, isle.getTower());
        assertEquals(12, gameModel.getIsles().size());
        isle.addStudent(Colour.Fairies);
        isle.addStudent(Colour.Fairies);
        message.accept(visitor);
        assertEquals(Faction.Black, isle.getTower());
        assertEquals(10, gameModel.getIsles().size());
    }

    @Test
    void testVisitPlus2MoveMnMessage()
    {
        Game game = new Game(2,true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0,CharactersEnum.PLUS_2_MN);
        int player = game.getCurrentPlayer();
        game.getGameModel().getPlayer(player).setChoosenAssistant(0);
        Assistant a = game.getGameModel().getPlayer(player).getChosen();
        int moves = a.getMn_moves() + a.getBoost();
        messageVisitor.visit(new Plus2MoveMnMessage(0,player));
        assertEquals(moves+2,a.getMn_moves()+a.getBoost());
        assertEquals(CharactersEnum.PLUS_2_MN.getPrice()+1,game.getGameModel().getCharacter(0).getPrice());
    }

    @Test
    void testVisitProhibitedIsleCharacterMessage() throws TileOutOfBoundsException
    {
        Game game = new Game(2,true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0,CharactersEnum.PROHIBITED);
        int player = game.getCurrentPlayer();
        int isle = new Random().nextInt(12);
        assertFalse(game.getGameModel().getIsle(isle).removeProhibited());
        messageVisitor.visit(new ProhibitedIsleCharacterMessage(0,player,isle));
        messageVisitor.visit(new ProhibitedIsleCharacterMessage(0,player,isle));
        assertEquals(2,game.getGameModel().getProhibited());
        assertTrue(game.getGameModel().getIsle(isle).removeProhibited());
        assertTrue(game.getGameModel().getIsle(isle).removeProhibited());
        assertFalse(game.getGameModel().getIsle(isle).removeProhibited());
    }

    @Test
    void testVisitMove6StudCharacterMessage() throws TileOutOfBoundsException {
        Game game = new Game(3, true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.createPlayer("C");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.EXCHANGE_3_STUD);
        int player = game.getCurrentPlayer();

        CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(0);
        character.addStudent(Colour.Gnomes);
        character.addStudent(Colour.Dragons);
        character.addStudent(Colour.Gnomes);

        Board board = game.getGameModel().getPlayer(player).getBoard();
        for(Colour c: Colour.values())
        {
            try {
                board.removeStudent(c);
            }catch(StudentsOutOfBoundsException e){}
        }
        board.addStudent(Colour.Fairies);
        board.addStudent(Colour.Frogs);
        board.addStudent(Colour.Unicorns);

        HashMap<Colour, Integer> expectedB = new HashMap<>();
        HashMap<Colour, Integer> expectedC = new HashMap<>();
        for (Colour c : Colour.values()) {
            expectedB.put(c, board.getStudents(c));
            expectedC.put(c, character.getStudents(c));
        }

        Colour studBoard[] = {Colour.Fairies, Colour.Frogs, Colour.Unicorns};
        Colour studChar[] = {Colour.Gnomes, Colour.Dragons, Colour.Gnomes};
        messageVisitor.visit(new Move6StudCharacterMessage(0, player, studBoard, studChar));

        assertEquals(expectedB.get(Colour.Dragons) + 1, board.getStudents(Colour.Dragons));
        assertEquals(expectedB.get(Colour.Gnomes) + 2, board.getStudents(Colour.Gnomes));
        assertEquals(expectedB.get(Colour.Frogs) - 1, board.getStudents(Colour.Frogs));
        assertEquals(expectedB.get(Colour.Unicorns) - 1, board.getStudents(Colour.Unicorns));
        assertEquals(expectedB.get(Colour.Fairies) - 1, board.getStudents(Colour.Fairies));

        assertEquals(expectedC.get(Colour.Dragons) - 1, character.getStudents(Colour.Dragons));
        assertEquals(expectedC.get(Colour.Gnomes) - 2, character.getStudents(Colour.Gnomes));
        assertEquals(expectedC.get(Colour.Frogs) + 1, character.getStudents(Colour.Frogs));
        assertEquals(expectedC.get(Colour.Unicorns) + 1, character.getStudents(Colour.Unicorns));
        assertEquals(expectedC.get(Colour.Fairies) + 1, character.getStudents(Colour.Fairies));
    }

    @Test
    void testVisitMove2StudCharacterMessage() throws StudentsOutOfBoundsException {
        Game game = new Game(3, true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.createPlayer("C");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.EXCHANGE_2_STUD);
        int player = game.getCurrentPlayer();
        Board board = game.getGameModel().getPlayer(player).getBoard();
        for(Colour c: Colour.values()) {
            try {
                board.removeStudent(c);
                board.removeStudent(c);
            }catch(StudentsOutOfBoundsException e){}

        }

        board.addToTable(Colour.Dragons);
        board.addToTable(Colour.Gnomes);
        board.addToEntrance(Colour.Fairies);
        board.addToEntrance(Colour.Unicorns);

        HashMap <Colour,Integer> entrance = new HashMap<>();
        HashMap <Colour,Integer> tables = new HashMap<>();
        for(Colour c: Colour.values())
        {
            entrance.put(c,board.getStudents(c));
            tables.put(c,board.getTable(c));
        }

        Colour[] studBoard ={Colour.Fairies,Colour.Unicorns};
        Colour[] studTables ={Colour.Dragons,Colour.Gnomes};

        messageVisitor.visit(new Move2StudCharacterMessage(0,player,studBoard,studTables));

        assertEquals(entrance.get(Colour.Dragons)+1,board.getStudents(Colour.Dragons));
        assertEquals(entrance.get(Colour.Gnomes)+1,board.getStudents(Colour.Gnomes));
        assertEquals(entrance.get(Colour.Fairies)-1,board.getStudents(Colour.Fairies));
        assertEquals(entrance.get(Colour.Unicorns)-1,board.getStudents(Colour.Unicorns));

        assertEquals(tables.get(Colour.Dragons)-1,board.getTable(Colour.Dragons));
        assertEquals(tables.get(Colour.Gnomes)-1,board.getTable(Colour.Gnomes));
        assertEquals(tables.get(Colour.Fairies)+1,board.getTable(Colour.Fairies));
        assertEquals(tables.get(Colour.Unicorns)+1,board.getTable(Colour.Unicorns));

    }

    @Test
    void testVisitRemove3StudCharacterMessage() throws StudentsOutOfBoundsException {
        Game game = new Game(3, true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.createPlayer("C");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.REMOVE_3_STUD);
        int player = game.getCurrentPlayer();
        Colour colour = Colour.values()[(new Random()).nextInt(5)];
        HashMap<Player,Integer> playerStud = new HashMap<>();
        int i = 1;
        for(Player p: game.getGameModel().getPlayers())
        {
            for(int j = 0; j< i; j++)
            {
                p.getBoard().addToTable(colour);
            }
            playerStud.put(p,p.getBoard().getTable(colour));
            i++;
        }

        messageVisitor.visit(new Remove3StudCharacterMessage(0,player,colour));

        for(Player p: game.getGameModel().getPlayers())
        {
            assertEquals((playerStud.get(p)-3<0)?0:playerStud.get(p)-3,p.getBoard().getTable(colour));
        }
    }
}