package it.polimi.ingsw.controller;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.PlayerOutOfBoundException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Observer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MessageVisitorTest {

    private Game game;
    private MessageVisitor mv;
    private TestObs obs;

    private class TestObs implements Observer<ClientModel> {
        public ClientModel message;

        @Override
        public void update(ClientModel message) {
            this.message = message;
        }
    }

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


    public void setupGame(Phase phase, int playersNumber, boolean expertMode) throws PlayerOutOfBoundException {
        game = new Game(playersNumber, expertMode);
        obs = new TestObs();
        game.addObserver(obs);
        game.createPlayer("Alberto");
        game.createPlayer("Lorenzo");
        if(playersNumber>2) {
            game.createPlayer("Ajeje");
            if(playersNumber>3)
                game.createPlayer("Paolo");
        }
        game.setupGame();
        mv = new MessageVisitor(game);
        mv.addObserver(obs);
        for(int i=0; i<4;i++){
            AssistantChoiceMessage acm = new AssistantChoiceMessage(i,game.getPlanningOrder().get(i));
            mv.visit(acm);
        }
        if(phase==Phase.MOTHERNATURE || phase ==Phase.CLOUD){
            int playerId = game.getActionOrder().get(0);
            Colour student = Colour.Dragons;
            for(int i=0; i<4;i++)
                game.getGameModel().getPlayer(playerId).getBoard().addStudent(student);
            int students = 0;
            try {
                students = game.getGameModel().getIsle(0).getStudents(student);
                MoveStudentMessage msm = new MoveStudentMessage(playerId,student,0,false);
                mv.visit(msm);
            }catch(TileOutOfBoundsException e){
                e.printStackTrace();
            }
            MoveStudentMessage msm = new MoveStudentMessage(playerId,student,0,true);
            mv.visit(msm);
            msm = new MoveStudentMessage(playerId,student,0,true);
            mv.visit(msm);
            if(phase==Phase.CLOUD){
                int maxMoves = game.getGameModel().getPlayer(playerId).getChosen().getMn_moves();
                MoveMotherNatureMessage mmn = new MoveMotherNatureMessage(playerId,maxMoves);
                mv.visit(mmn);
            }
        }
    }

    @Test
    void visitAssistantMessageTest() throws PlayerOutOfBoundException {
        Game game = new Game(4, true);
        game.createPlayer("Alberto");
        game.createPlayer("Lorenzo");
        game.createPlayer("Ajeje");
        game.createPlayer("Paolo");
        game.setupGame();
        MessageVisitor mv = new MessageVisitor(game);
        obs = new TestObs();
        game.addObserver(obs);
        mv.addObserver(obs);
        game.sendInitialGame();
        ArrayList<Integer> order = (ArrayList<Integer>) game.getPlanningOrder().clone();
        for(int i=0; i<4;i++){
            assertEquals(order.get(i),((TurnMessage)obs.message).getPlayerId());
            AssistantChoiceMessage acm = new AssistantChoiceMessage(i,game.getPlanningOrder().get(i));
            mv.visit(acm);
        }
        for(int i=0; i<4;i++){
            assertEquals(game.getGameModel().getPlayer(order.get(i)).getChosen().getValue()-1,i);
        }
    }

    @Test
    void testVisitMoveStudentMessage() throws PlayerOutOfBoundException {
        //toIsle
        setupGame(Phase.STUDENTS,4,true);
        int playerId = game.getActionOrder().get(0);
        Colour student = Colour.Dragons;
        for(int i=0; i<4;i++)
            game.getGameModel().getPlayer(playerId).getBoard().addStudent(student);
        int students = 0;
        try {
            students = game.getGameModel().getIsle(0).getStudents(student);
            MoveStudentMessage msm = new MoveStudentMessage(playerId,student,0,false);
            mv.visit(msm);
            assertEquals(game.getGameModel().getIsle(0).getStudents(student),students+1);
            assertEquals(students+1,((ClientIsle)obs.message).getStudents().get(student));
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }
        //toTable
        MoveStudentMessage msm = new MoveStudentMessage(playerId,student,0,true);
        mv.visit(msm);
        assertEquals(game.getGameModel().getPlayer(playerId).getBoard().getTable(student),1);
        assertEquals(playerId,((ClientGameModel)obs.message).getProfessors().get(student));
        msm = new MoveStudentMessage(playerId,student,0,true);
        mv.visit(msm);
        assertEquals(game.getGameModel().getPlayer(playerId).getBoard().getTable(student),2);
        assertEquals(game.getTurnHandler().getStudentsToMove(),0);
        assertEquals(game.getTurnHandler().getPhase(),Phase.MOTHERNATURE);
        assertEquals(TurnMessage.Turn.ACTION_MN,((TurnMessage)obs.message).getTurn());

        msm = new MoveStudentMessage((playerId+1)%4,student,0,true);
        mv.visit(msm);
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());
    }

    @Test
    void testVisitMotherNatureMessage() throws PlayerOutOfBoundException {
        setupGame(Phase.MOTHERNATURE, 4, true);
        Random rand = new Random();
        int playerId = game.getCurrentPlayer();
        int mn = game.getGameModel().getMotherNature();
        int moves = rand.nextInt(game.getGameModel().getPlayer(playerId).getChosen().getMn_moves());
        MoveMotherNatureMessage mmn = new MoveMotherNatureMessage(playerId, moves);
        mv.visit(mmn);
        assertEquals((mn + moves) % game.getGameModel().getIsles().size(), game.getGameModel().getMotherNature());
        assertEquals(TurnMessage.Turn.ACTION_CLOUDS,((TurnMessage)obs.message).getTurn());
        mmn = new MoveMotherNatureMessage((playerId+1)%4, moves);
        mv.visit(mmn);
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3})
    void testVisitCloudChoiceMessage(int cloudId) throws PlayerOutOfBoundException {
        setupGame(Phase.CLOUD,4,true);
        int playerId = game.getCurrentPlayer();
        CloudChoiceMessage ccm = new CloudChoiceMessage((playerId+1)%4,cloudId);
        mv.visit(ccm);
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());
        ccm = new CloudChoiceMessage(playerId,cloudId);
        HashMap<Colour,Integer> students = new HashMap<>();
        try {
            for (Colour c : Colour.values()) {
                students.put(c,game.getGameModel().getCloud(cloudId).getStudents(c)+game.getGameModel().getPlayer(playerId).getBoard().getStudents(c));
            }
            mv.visit(ccm);
//            assertEquals(TurnMessage.Turn.ACTION_STUDENTS,((TurnMessage)obs.message).getTurn());
            for (Colour c : Colour.values()) {
                assertEquals(0, game.getGameModel().getCloud(cloudId).getStudents(c));
                assertEquals(students.get(c), game.getGameModel().getPlayer(playerId).getBoard().getStudents(c));
            }
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }

    }

    int charSize(CharacterStudents character){
        int size = 0;
        for(Colour c : Colour.values()){
            size += character.getStudents(c);
        }
        return size;
    }

    @ParameterizedTest
    @MethodSource("argsA")
    void testVisitIsleInfluenceCharacterMessage(CharactersEnum charType, int playerNum) throws TileOutOfBoundsException, NotEnoughCoinsException, PlayerOutOfBoundException {

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
        game.startActionTurn();
        for(int p = 0; p < playerNum; p++){
            game.createPlayer("p" + p);
            game.getGameModel().getPlayer(p).createBoard((playerNum == 3)?6:8);
        }

        // Usefully variables
        Isle isle = gameModel.getIsle(ISLE_ID);
        Player player_a = gameModel.getPlayer(PLAYER_ID_A);
        Player player_b = gameModel.getPlayer(PLAYER_ID_B);
        HashMap<Colour, Player> professors = gameModel.getProfessors();

        // Setup test background
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

        TestObs obs = new TestObs();
        game.addObserver(obs);

        // Test
        IsleInfluenceCharacterMessage message = new IsleInfluenceCharacterMessage(CHAR_ID,PLAYER_ID_A,ISLE_ID, COLOUR_A);
        MessageVisitor visitor = new MessageVisitor(game);
        visitor.addObserver(obs);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());
        message.accept(visitor);
        for(int i = 0; i < 3; i++){ gameModel.giveCoin(player_a);}
        message.accept(visitor);
        if(charType == CharactersEnum.PLUS_2_INFLUENCE){
            assertEquals(4, isle.getInfluence(player_a, professors));
            if(playerNum == 4){
                assertEquals(5, isle.getInfluence(gameModel.getTeam(TEAM_ID), professors));
            }
            assertEquals(CharactersEnum.PLUS_2_INFLUENCE,((ClientCharacter)obs.message).getCard());
            assertEquals(CharactersEnum.PLUS_2_INFLUENCE.getPrice()+1,((ClientCharacter)obs.message).getPrice());
        }
        if(charType == CharactersEnum.NO_TOWER_INFLUENCE){
            assertEquals(1, isle.getInfluence(player_a, professors));
            if(playerNum == 1){
                assertEquals(2, isle.getInfluence(gameModel.getTeam(TEAM_ID), professors));
            }
            assertEquals(CharactersEnum.NO_TOWER_INFLUENCE,((ClientCharacter)obs.message).getCard());
            assertEquals(CharactersEnum.NO_TOWER_INFLUENCE.getPrice()+1,((ClientCharacter)obs.message).getPrice());

        }
        if(charType == CharactersEnum.NO_COLOUR_INFLUENCE){
            assertEquals(1, isle.getInfluence(player_a, professors));
            if(playerNum == 4){
                assertEquals(2, isle.getInfluence(gameModel.getTeam(TEAM_ID), professors));
            }
            assertEquals(CharactersEnum.NO_COLOUR_INFLUENCE,((ClientCharacter)obs.message).getCard());
            assertEquals(CharactersEnum.NO_COLOUR_INFLUENCE.getPrice()+1,((ClientCharacter)obs.message).getPrice());
        }

        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        message = new IsleInfluenceCharacterMessage(CHAR_ID,(PLAYER_ID_A+1)%playerNum,ISLE_ID, COLOUR_A);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());
    }

    @ParameterizedTest
    @MethodSource("argsB")
    void testVisitMoveStudentCharacterMessage(Colour colour, CharactersEnum charType) throws TileOutOfBoundsException, NotEnoughCoinsException, PlayerOutOfBoundException {

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
        for(int i = 0; i < 4; i++){
            for(Colour c : Colour.values()){
                try{
                    character.removeStudent(c);
                }catch (Exception ignored){

                }
            }
        }
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
        assertEquals(4, charSize(character));

        TestObs obs = new TestObs();
        game.addObserver(obs);

        // Test
        MoveStudentCharacterMessage message = new MoveStudentCharacterMessage(PLAYER_ID, CHAR_ID, colour, ISLE_ID);
        MessageVisitor visitor = new MessageVisitor(game);
        visitor.addObserver(obs);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());
        for(int i = 0; i < 3; i++){ gameModel.giveCoin(player); }
        message.accept(visitor);
        if(charType == CharactersEnum.ONE_STUD_TO_TABLES){
            assertEquals(119, gameModel.getBagSize());
            assertEquals((colour==Colour.Dragons)?1:0, player.getBoard().getTable(Colour.Dragons));
            assertEquals((colour==Colour.Frogs)?1:0, player.getBoard().getTable(Colour.Frogs));
            assertEquals((colour==Colour.Fairies)?1:0, player.getBoard().getTable(Colour.Fairies));
            assertEquals(0, isle.getStudents(Colour.Fairies));
            assertEquals(0, isle.getStudents(Colour.Dragons));
            assertEquals(0, isle.getStudents(Colour.Frogs));
            assertEquals(CharactersEnum.ONE_STUD_TO_TABLES,((ClientCharacter)obs.message).getCard());
            assertEquals(CharactersEnum.ONE_STUD_TO_TABLES.getPrice()+1,((ClientCharacter)obs.message).getPrice());
        }
        if(charType == CharactersEnum.ONE_STUD_TO_ISLE){
            assertEquals(119, gameModel.getBagSize());
            assertEquals(0, player.getBoard().getTable(Colour.Dragons));
            assertEquals(0, player.getBoard().getTable(Colour.Frogs));
            assertEquals(0, player.getBoard().getTable(Colour.Fairies));
            assertEquals((colour==Colour.Fairies)?1:0, isle.getStudents(Colour.Fairies));
            assertEquals((colour==Colour.Dragons)?1:0, isle.getStudents(Colour.Dragons));
            assertEquals((colour==Colour.Frogs)?1:0, isle.getStudents(Colour.Frogs));
            assertEquals(CharactersEnum.ONE_STUD_TO_ISLE,((ClientCharacter)obs.message).getCard());
            assertEquals(CharactersEnum.ONE_STUD_TO_ISLE.getPrice()+1,((ClientCharacter)obs.message).getPrice());
        }
        assertEquals(4, charSize(character));
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        message = new MoveStudentCharacterMessage((PLAYER_ID+1)%PLAYER_NUM, CHAR_ID, colour, ISLE_ID);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());

    }

    @ParameterizedTest
    @MethodSource("argsC")
    void testVisitStrategyProfessorMessage(Colour colour) throws StudentsOutOfBoundsException, NotEnoughCoinsException, PlayerOutOfBoundException {

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

        TestObs obs = new TestObs();
        game.addObserver(obs);
        // Test
        MessageVisitor visitor = new MessageVisitor(game);
        visitor.addObserver(obs);
        turn.setCurrentPlayer(playerA);
        game.setCurrentPlayer(playerA.getID());
        StrategyProfessorMessage message = new StrategyProfessorMessage(CHAR_ID, PLAYER_ID_A);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());
        for(int i = 0; i < 5; i++){ gameModel.giveCoin(playerA); }
        message.accept(visitor);
        assertEquals(CharactersEnum.PROFESSOR_CONTROL,((ClientCharacter)obs.message).getCard());
        assertEquals(CharactersEnum.PROFESSOR_CONTROL.getPrice()+1,((ClientCharacter)obs.message).getPrice());
        boardA.addToTable(colour);
        turn.checkProfessor(colour);
        assertEquals(playerA, gameModel.getProfessorOwner(colour).get());

        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        message = new StrategyProfessorMessage(CHAR_ID, (PLAYER_ID_A+1)%PLAYERS_NUM);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());

        boardB.addToTable(colour);
        turn.setCurrentPlayer(playerB);
        game.setCurrentPlayer(playerB.getID());
        turn.checkProfessor(colour);
        assertEquals(playerB, gameModel.getProfessorOwner(colour).get());
    }

    @Test
    void testVisitSimilMotherNatureMesage() throws TileOutOfBoundsException, PlayerOutOfBoundException {

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

        TestObs obs = new TestObs();
        game.addObserver(obs);

        // Test
        MessageVisitor visitor = new MessageVisitor(game);
        visitor.addObserver(obs);
        SimilMotherNatureMesage message = new SimilMotherNatureMesage(CHAR_ID, PLAYER_ID_A, ISLE_ID);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());

        for(int i = 0; i < 7; i++){ gameModel.giveCoin(playerA); }
        message.accept(visitor);



        assertEquals(CharactersEnum.SIMIL_MN,((ClientCharacter)obs.message).getCard());
        assertEquals(CharactersEnum.SIMIL_MN.getPrice()+1,((ClientCharacter)obs.message).getPrice());

        assertEquals(Faction.Empty, isle.getTower());
        assertEquals(12, gameModel.getIsles().size());
        isle.addStudent(Colour.Fairies);
        isle.addStudent(Colour.Fairies);
        game.startActionTurn();
        message.accept(visitor);
        assertEquals(Faction.Black, isle.getTower());
        assertEquals(10, gameModel.getIsles().size());

        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        message = new SimilMotherNatureMesage(CHAR_ID, (PLAYER_ID_A+1)%PLAYERS_NUM, ISLE_ID);
        message.accept(visitor);
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());

    }

    @Test
    void testVisitPlus2MoveMnMessage() throws PlayerOutOfBoundException {
        Game game = new Game(2,true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0,CharactersEnum.PLUS_2_MN);

        TestObs obs = new TestObs();
        game.addObserver(obs);
        messageVisitor.addObserver(obs);

        int player = game.getCurrentPlayer();
        game.getGameModel().getPlayer(player).removeCoins(2);
        messageVisitor.visit(new Plus2MoveMnMessage(0,player));
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());


        for(int i = 0; i < 5; i++) {
            game.getGameModel().giveCoin(game.getGameModel().getPlayer(player));
        }

        game.getGameModel().getPlayer(player).setChoosenAssistant(0);
        Assistant a = game.getGameModel().getPlayer(player).getChosen();
        int moves = a.getMn_moves() + a.getBoost();
        messageVisitor.visit(new Plus2MoveMnMessage(0,player));
        assertEquals(moves+2,a.getMn_moves()+a.getBoost());
        assertEquals(CharactersEnum.PLUS_2_MN.getPrice()+1,game.getGameModel().getCharacter(0).getPrice());
        assertEquals(CharactersEnum.PLUS_2_MN,((ClientCharacter)obs.message).getCard());
        assertEquals(CharactersEnum.PLUS_2_MN.getPrice()+1,((ClientCharacter)obs.message).getPrice());


        messageVisitor.visit(new Plus2MoveMnMessage(0,player));
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        messageVisitor.visit(new Plus2MoveMnMessage(0,(player+1)%2));
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());


        assertEquals(4-CharactersEnum.PLUS_2_MN.getPrice(),game.getGameModel().getPlayer(player).getCoins());

    }

    @Test
    void testVisitProhibitedIsleCharacterMessage() throws TileOutOfBoundsException, PlayerOutOfBoundException {
        Game game = new Game(2,true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0,CharactersEnum.PROHIBITED);

        TestObs obs = new TestObs();
        game.addObserver(obs);
        messageVisitor.addObserver(obs);

        int player = game.getCurrentPlayer();

        game.getGameModel().getPlayer(player).removeCoins(2);

        int isle = new Random().nextInt(12);

        messageVisitor.visit(new ProhibitedIsleCharacterMessage(0,player,isle));
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());


        for(int i = 0; i < 5; i++) {
            game.getGameModel().giveCoin(game.getGameModel().getPlayer(player));
        }
        assertFalse(game.getGameModel().getIsle(isle).removeProhibited());
        messageVisitor.visit(new ProhibitedIsleCharacterMessage(0,player,isle));

        assertEquals(CharactersEnum.PROHIBITED,((ClientCharacter)obs.message).getCard());
        assertEquals(CharactersEnum.PROHIBITED.getPrice()+1,((ClientCharacter)obs.message).getPrice());

        game.getTurnHandler().setUsedCharacter(false);
        game.getGameModel().giveCoin(game.getGameModel().getPlayer(player));
        messageVisitor.visit(new ProhibitedIsleCharacterMessage(0,player,isle));

        assertEquals(2,game.getGameModel().getProhibited());
        assertTrue(game.getGameModel().getIsle(isle).removeProhibited());
        assertTrue(game.getGameModel().getIsle(isle).removeProhibited());
        assertFalse(game.getGameModel().getIsle(isle).removeProhibited());

        messageVisitor.visit(new ProhibitedIsleCharacterMessage(0,player,isle));
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        messageVisitor.visit(new ProhibitedIsleCharacterMessage(0,(player+1)%2,isle));
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());

        assertEquals(4-2*CharactersEnum.PROHIBITED.getPrice(),game.getGameModel().getPlayer(player).getCoins());
    }

    @Test
    void testVisitMove6StudCharacterMessage() throws TileOutOfBoundsException, PlayerOutOfBoundException {
        Game game = new Game(3, true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.createPlayer("C");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.EXCHANGE_3_STUD);

        TestObs obs = new TestObs();
        game.addObserver(obs);
        messageVisitor.addObserver(obs);

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

        game.getGameModel().getPlayer(player).removeCoins(2);

        Colour studBoard[] = {Colour.Fairies, Colour.Frogs, Colour.Unicorns};
        Colour studChar[] = {Colour.Gnomes, Colour.Dragons, Colour.Gnomes};
        messageVisitor.visit(new Move6StudCharacterMessage(0, player, studBoard, studChar));
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());

        for(int i = 0; i < 5; i++) {
            game.getGameModel().giveCoin(game.getGameModel().getPlayer(player));
        }

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

        assertEquals(CharactersEnum.EXCHANGE_3_STUD,((ClientCharacter)obs.message).getCard());
        assertEquals(CharactersEnum.EXCHANGE_3_STUD.getPrice()+1,((ClientCharacter)obs.message).getPrice());

        messageVisitor.visit(new Move6StudCharacterMessage(0, player, studBoard, studChar));
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        messageVisitor.visit(new Move6StudCharacterMessage(0, (player+1)%3, studBoard, studChar));
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());


        assertEquals(4-CharactersEnum.EXCHANGE_3_STUD.getPrice(),game.getGameModel().getPlayer(player).getCoins());
    }

    @Test
    void testVisitMove2StudCharacterMessage() throws StudentsOutOfBoundsException, PlayerOutOfBoundException {
        Game game = new Game(3, true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.createPlayer("C");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.EXCHANGE_2_STUD);

        obs = new TestObs();
        game.addObserver(obs);
        messageVisitor.addObserver(obs);

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
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());

        for(int i = 0; i < 5; i++) {
            game.getGameModel().giveCoin(game.getGameModel().getPlayer(player));
        }

        messageVisitor.visit(new Move2StudCharacterMessage(0,player,studBoard,studTables));

        assertEquals(entrance.get(Colour.Dragons)+1,board.getStudents(Colour.Dragons));
        assertEquals(entrance.get(Colour.Gnomes)+1,board.getStudents(Colour.Gnomes));
        assertEquals(entrance.get(Colour.Fairies)-1,board.getStudents(Colour.Fairies));
        assertEquals(entrance.get(Colour.Unicorns)-1,board.getStudents(Colour.Unicorns));

        assertEquals(tables.get(Colour.Dragons)-1,board.getTable(Colour.Dragons));
        assertEquals(tables.get(Colour.Gnomes)-1,board.getTable(Colour.Gnomes));
        assertEquals(tables.get(Colour.Fairies)+1,board.getTable(Colour.Fairies));
        assertEquals(tables.get(Colour.Unicorns)+1,board.getTable(Colour.Unicorns));

        assertEquals(CharactersEnum.EXCHANGE_2_STUD,((ClientCharacter)obs.message).getCard());
        assertEquals(CharactersEnum.EXCHANGE_2_STUD.getPrice()+1,((ClientCharacter)obs.message).getPrice());

        messageVisitor.visit(new Move2StudCharacterMessage(0,player,studBoard,studTables));
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        messageVisitor.visit(new Move2StudCharacterMessage(0,(player+1)%3,studBoard,studTables));
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());


        assertEquals(6-CharactersEnum.EXCHANGE_2_STUD.getPrice(),game.getGameModel().getPlayer(player).getCoins());
    }

    @Test
    void testVisitRemove3StudCharacterMessage() throws StudentsOutOfBoundsException, PlayerOutOfBoundException {
        Game game = new Game(3, true);
        MessageVisitor messageVisitor = new MessageVisitor(game);
        game.createPlayer("A");
        game.createPlayer("B");
        game.createPlayer("C");
        game.setupGame();
        game.startActionTurn();
        game.getGameModel().setCharacter_DEBUG(0, CharactersEnum.REMOVE_3_STUD);

        obs = new TestObs();
        game.addObserver(obs);
        messageVisitor.addObserver(obs);

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
        assertEquals(ErrorMessage.ErrorType.NotEnoughCoinError,((ErrorMessage)obs.message).getError());

        for(int j = 0; j < 5; j++) {
            game.getGameModel().giveCoin(game.getGameModel().getPlayer(player));
        }

        messageVisitor.visit(new Remove3StudCharacterMessage(0,player,colour));

        for(Player p: game.getGameModel().getPlayers())
        {
            assertEquals((playerStud.get(p)-3<0)?0:playerStud.get(p)-3,p.getBoard().getTable(colour));
        }
        assertEquals(CharactersEnum.REMOVE_3_STUD,((ClientCharacter)obs.message).getCard());
        assertEquals(CharactersEnum.REMOVE_3_STUD.getPrice()+1,((ClientCharacter)obs.message).getPrice());

        messageVisitor.visit(new Remove3StudCharacterMessage(0,player,colour));
        assertEquals(ErrorMessage.ErrorType.CharacterAlreadyUsedError,((ErrorMessage)obs.message).getError());
        messageVisitor.visit(new Remove3StudCharacterMessage(0,(player+1)%3,colour));
        assertEquals(ErrorMessage.ErrorType.NotYourTurnError,((ErrorMessage)obs.message).getError());


        assertEquals(6-CharactersEnum.REMOVE_3_STUD.getPrice(),game.getGameModel().getPlayer(player).getCoins());
    }
}