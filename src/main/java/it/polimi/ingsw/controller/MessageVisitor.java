package it.polimi.ingsw.controller;

import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.server.Observable;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Visitor class to handle move messages sent by the players
 */
public class MessageVisitor extends Observable<ClientModel> {

    /**
     * Game class handling the match
     */
    final Game game;

    /**
     * Creates the message visitor given the current Game
     * @param game The current Game object handling the match
     */
    public MessageVisitor(Game game){
        this.game = game;
    }

    /**
     * Handles a character being used by incrementing his price (if used for the first time) and removing coins from the player
     * @param charId The used character
     */
    private void useCharacter(int charId) {
        game.getGameModel().getPlayer(game.getCurrentPlayer()).removeCoins(game.getGameModel().getCharacter(charId).getPrice());
        if (game.getGameModel().getCharacter(charId).getUsed()){
            try {
                game.getGameModel().addCoins(game.getGameModel().getCharacter(charId).getPrice());
            } catch (NotEnoughCoinsException e) {
                e.printStackTrace();
            }
        } else {
            try {
                game.getGameModel().addCoins(game.getGameModel().getCharacter(charId).getPrice() - 1);
            } catch (NotEnoughCoinsException e) {
                e.printStackTrace();
            }
        }
        game.getGameModel().getCharacter(charId).use();
        game.getTurnHandler().setUsedCharacter(true);
    }

    /**
     * Handles messages for choosing assistants
     * @param assistantChoiceMessage The message for choosing assistants
     */
    public void visit(AssistantChoiceMessage assistantChoiceMessage){
        int playerID = game.getCurrentPlayer();
        if(assistantChoiceMessage.getPlayerID()==playerID && game.isPlanning()) {
            ArrayList<Player> players = game.getGameModel().getPlayers();
            Player player = players.get(playerID);
            int id = assistantChoiceMessage.getAssistantID();
            if(!player.hasUsed(id)) {
                    if(game.alreadyUsed(id) && player.getDeck().size()>1){
                        notify(new ErrorMessage(game.getCurrentPlayer(),ErrorMessage.ErrorType.AssistantOtherPlayerError));
                    }
                    else{
                        player.setChoosenAssistant(id);
                        game.nextPlayer();
                    }
            }
            else{
                notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.AssistantAlreadyChosenError));
            }

        }else{
            notify(new ErrorMessage(assistantChoiceMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
        }

    }

    /**
     * Handles messages for moving students from boards to isles and tables
     * @param moveStudentMessage The message being handled
     */
    public void visit(MoveStudentMessage moveStudentMessage){
            int playerID = moveStudentMessage.getPlayerID();
            Colour student = moveStudentMessage.getStudent();
            if (playerID == game.getCurrentPlayer() && game.getTurnHandler().getStudentsToMove()>0) {
                if (moveStudentMessage.isToTable()) {
                    game.getTurnHandler().moveStudentToTable(student);
                } else {
                    int isle = moveStudentMessage.getTileID();
                    game.getTurnHandler().moveStudentToIsle(student,isle);
                }
            }else{
                notify(new ErrorMessage(moveStudentMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
    }

    /**
     * Handles messages for moving mother nature
     * @param moveMotherNatureMessage The message being handled
     */
    public void visit(MoveMotherNatureMessage moveMotherNatureMessage){
        if(moveMotherNatureMessage.getPlayerID()==game.getCurrentPlayer() && game.getTurnHandler().getPhase()==Phase.MOTHERNATURE){
            game.getTurnHandler().moveMn(moveMotherNatureMessage.getMoves());
            game.checkEndTowerIsle();
        }
        else{
            notify(new ErrorMessage(moveMotherNatureMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
        }
    }

    /**
     * Handles messages for choosing clouds at turn end
     * @param cloudChoiceMessage The message being handled
     */
    public void visit(CloudChoiceMessage cloudChoiceMessage){
        if(game.getCurrentPlayer()==cloudChoiceMessage.getPlayerId() && game.getTurnHandler().getPhase()==Phase.CLOUD){
            if(game.getTurnHandler().moveFromCloud(cloudChoiceMessage.getCloudID()))
                game.nextPlayer();
        }
        else{
            notify(new ErrorMessage(cloudChoiceMessage.getPlayerId(), ErrorMessage.ErrorType.NotYourTurnError));
        }
    }

    /**
     * Handles messages for using characters that modify the current strategy used to calculate the influence over an island
     * @param isleInfluenceCharacterMessage The message being handled
     */
    public void visit(IsleInfluenceCharacterMessage isleInfluenceCharacterMessage) {
        int playerId = isleInfluenceCharacterMessage.getPlayerID();
        int charId = isleInfluenceCharacterMessage.getCharacterID();
        if (game.isExpertMode())
        {
            if (game.getCurrentPlayer() == playerId && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()){

                    Player player = game.getGameModel().getPlayer(playerId);
                    Character character = game.getGameModel().getCharacter(charId);
                    if(player.getCoins() >= character.getPrice()){
                        switch (character.getCard()) {
                            case NO_TOWER_INFLUENCE -> game.getTurnHandler().setInfStrategy(new noTowersStrategy());
                            case PLUS_2_INFLUENCE ->
                                    game.getTurnHandler().setInfStrategy(new PlusInfStrategy(playerId));
                            case NO_COLOUR_INFLUENCE ->
                                    game.getTurnHandler().setInfStrategy(new NoColourStrategy(isleInfluenceCharacterMessage.getNoColour()));
                        }
                            useCharacter(charId);
                    }else{
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                    }
                }
                else {
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                }
            }else {
                notify(new ErrorMessage(isleInfluenceCharacterMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles messages for using characters who can contain students
     * @param moveStudentCharacterMessage The message being handled
     */
    public void visit(MoveStudentCharacterMessage moveStudentCharacterMessage){
        int playerId = moveStudentCharacterMessage.getPlayerID();
        int charId = moveStudentCharacterMessage.getCharacterID();
        Colour stud = moveStudentCharacterMessage.getStudent();
        int tileId = moveStudentCharacterMessage.getTileID();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId && !game.isPlanning()) {
                CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(charId);
                Player player = game.getGameModel().getPlayer(playerId);
                if(!game.getTurnHandler().isUsedCharacter()){
                    if(player.getCoins() >= character.getPrice()){
                        if(character.getCard() == CharactersEnum.ONE_STUD_TO_ISLE||(tileId >= 0 && tileId < game.getGameModel().getIsles().size())) {
                            if(character.getStudents(stud)>0) {
                                try {
                                    Board board = game.getGameModel().getPlayer(playerId).getBoard();
                                    character.removeStudent(stud);
                                    switch (character.getCard()) {
                                        case ONE_STUD_TO_ISLE -> {
                                            Isle isle = game.getGameModel().getIsle(tileId);
                                            isle.addStudent(stud);
                                        }
                                        case ONE_STUD_TO_TABLES -> {
                                            board.addToTable(stud);
                                            game.getTurnHandler().checkProfessor(stud);
                                        }
                                    }
                                    character.addStudent(game.getGameModel().extractRandomStudent());
                                    useCharacter(charId);
                                } catch (TileOutOfBoundsException e) {
                                    e.printStackTrace();
                                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.IsleError));
                                } catch (StudentsOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.StudentError));
                            }
                        }else{
                            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.IsleError));
                        }
                    }else{
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                    }

                } else {
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                }
            }else{
                notify(new ErrorMessage(moveStudentCharacterMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles messages for using characters that modify the current strategy used for checking who owns a professor
     * @param strategyProfessorMessage The message being handled
     */
    public void visit(StrategyProfessorMessage strategyProfessorMessage){
        int playerId = strategyProfessorMessage.getPlayerID();
        int charId = strategyProfessorMessage.getCharacterID();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId && !game.isPlanning()) {
                Character character = game.getGameModel().getCharacter(charId);
                Player player = game.getGameModel().getPlayer(playerId);
                if(player.getCoins() >= character.getPrice()){
                    ActionTurnHandler handler = game.getTurnHandler();
                    if(!handler.isUsedCharacter()){
                        handler.setProfessorStrategy(new ModifiedCheckProfessorStrategy());
                        useCharacter(charId);
                    }else{
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                    }
                }else{
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                }
            } else {
                notify(new ErrorMessage(strategyProfessorMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles messages for using the character that behaves like mother nature visiting an island
     * @param similMotherNatureMesage The message being handled
     */
    public void visit(SimilMotherNatureMesage similMotherNatureMesage){
        int playerId = similMotherNatureMesage.getPlayerID();
        int charId = similMotherNatureMesage.getCharacterID();
        int isleId = similMotherNatureMesage.getIsleID();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId && !game.isPlanning()) {
                ActionTurnHandler handler = game.getTurnHandler();
                if(!handler.isUsedCharacter()){
                    Character character = game.getGameModel().getCharacter(charId);
                    Player player = game.getGameModel().getPlayer(playerId);
                    if(player.getCoins() >= character.getPrice()){
                        handler.checkTower(isleId);
                        handler.checkIsleJoin(isleId);
                        useCharacter(charId);
                        game.checkEndTowerIsle();
                    }else{
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                    }
                }else{
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                }
            } else {
                notify(new ErrorMessage(similMotherNatureMesage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles the message for using the character that increases mother nature maximum moves
     * @param plus2MoveMnMessage The message being handled
     */
    public void visit(Plus2MoveMnMessage plus2MoveMnMessage){
        if (game.isExpertMode()) {
            if (plus2MoveMnMessage.getPlayerID() == game.getCurrentPlayer() && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(plus2MoveMnMessage.getCharacterID()).getPrice()) {
                        game.getGameModel().getPlayer(game.getCurrentPlayer()).boost();
                        useCharacter(plus2MoveMnMessage.getCharacterID());

                    } else {
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                    }
                }else{
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                }
            } else {
                notify(new ErrorMessage(plus2MoveMnMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }  else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles the message for using the character which can prohibit an island
     * @param prohibitedIsleCharacterMessage The message being handled
     */
    public void visit(ProhibitedIsleCharacterMessage prohibitedIsleCharacterMessage){
        if(game.isExpertMode()) {
            if (prohibitedIsleCharacterMessage.getPlayerID() == game.getCurrentPlayer() && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(prohibitedIsleCharacterMessage.getCharacterID()).getPrice()) {
                        if (game.getGameModel().getProhibited() > 0) {
                            try {
                                game.getGameModel().getIsle(prohibitedIsleCharacterMessage.getIsleID()).setProhibited();
                                game.getGameModel().useProhibited();
                                useCharacter(prohibitedIsleCharacterMessage.getCharacterID());
                            } catch (TileOutOfBoundsException e) {
                                notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.IsleError));
                            }
                        } else {
                            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.ProhibitedError));
                        }
                    } else {
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                    }
                }else{
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                }
            } else {
                notify(new ErrorMessage(prohibitedIsleCharacterMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles messages for moving students from the character that can contain 6 students
     * @param move6StudCharacterMessage The message being handled
     */
    public void visit(Move6StudCharacterMessage move6StudCharacterMessage){
        boolean valid = true;
        HashMap<Colour,Integer> check= new HashMap<>();
        if(game.isExpertMode()) {
            if (move6StudCharacterMessage.getPlayerID() == game.getCurrentPlayer() && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()){
                if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(move6StudCharacterMessage.getCharacterID()).getPrice()) {
                    CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(move6StudCharacterMessage.getCharacterID());
                    Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
                    if(move6StudCharacterMessage.getStudFromBoard().length == 3 && move6StudCharacterMessage.getStudsFromChar().length == 3) {
                        for (Colour c : move6StudCharacterMessage.getStudFromBoard())
                        {
                            check.put(c,(check.containsKey(c))?check.get(c)+1:1);
                            if(board.getStudents(c)< check.get(c))
                                valid = false;
                        }
                        check = new HashMap<>();
                        for (Colour c : move6StudCharacterMessage.getStudsFromChar())
                        {
                            check.put(c,(check.containsKey(c))?check.get(c)+1:1);
                            if(character.getStudents(c)< check.get(c))
                                valid = false;
                        }
                        if(valid) {
                            for (Colour c : move6StudCharacterMessage.getStudFromBoard()) {
                                try {
                                    board.removeStudent(c);
                                } catch (StudentsOutOfBoundsException e) {
                                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.TileIsEmptyError));
                                }
                                character.addStudent(c);
                            }
                            for (Colour c : move6StudCharacterMessage.getStudsFromChar()) {
                                try {
                                    board.addToEntrance(c);
                                } catch (StudentsOutOfBoundsException e) {
                                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.TileIsFullError));
                                }
                                try {
                                    character.removeStudent(c);
                                } catch (StudentsOutOfBoundsException e) {
                                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.TileIsEmptyError));
                                }
                            }
                            useCharacter(move6StudCharacterMessage.getCharacterID());
                        }else{
                            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.StudentError));
                        }
                    }else{
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.WrongStudentNumber));
                    }
                } else {
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                }
            }else {
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
            }
            } else {
                notify(new ErrorMessage(move6StudCharacterMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles messages for using the character that exchanges students between tables and board
     * @param move2StudCharacterMessage The message being handled
     */
    public void visit(Move2StudCharacterMessage move2StudCharacterMessage){
        boolean valid = true;
        HashMap<Colour,Integer> check= new HashMap<>();
        if(game.isExpertMode()) {
            if (move2StudCharacterMessage.getPlayerID() == game.getCurrentPlayer() && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(move2StudCharacterMessage.getCharacterID()).getPrice()) {
                        Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
                        if(move2StudCharacterMessage.getStudFromBoard().length == move2StudCharacterMessage.getStudFromTables().length && move2StudCharacterMessage.getStudFromBoard().length <= 2) {
                            for (Colour c : move2StudCharacterMessage.getStudFromBoard())
                            {
                                check.put(c,(check.containsKey(c))?check.get(c)+1:1);
                                if(board.getStudents(c)< check.get(c))
                                    valid = false;
                            }
                            check = new HashMap<>();
                            for (Colour c : move2StudCharacterMessage.getStudFromTables())
                            {
                                check.put(c,(check.containsKey(c))?check.get(c)+1:1);
                                if(board.getTable(c)< check.get(c))
                                    valid = false;
                            }
                            if(valid) {
                                for (Colour c : move2StudCharacterMessage.getStudFromBoard()) {
                                    try {
                                        board.removeStudent(c);
                                    } catch (StudentsOutOfBoundsException e) {
                                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.TileIsEmptyError));
                                    }
                                    try {
                                        board.addToTable(c);
                                    } catch (StudentsOutOfBoundsException e) {
                                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.TileIsFullError));
                                    }

                                }
                                for (Colour c : move2StudCharacterMessage.getStudFromTables()) {
                                    try {
                                        board.addToEntrance(c);
                                    } catch (StudentsOutOfBoundsException e) {
                                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.TileIsFullError));
                                    }
                                    try {
                                        board.removeFromTable(c);
                                    } catch (StudentsOutOfBoundsException e) {
                                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.TileIsEmptyError));
                                    }
                                }
                                for (Colour c : move2StudCharacterMessage.getStudFromTables()) {
                                    game.getTurnHandler().checkProfessor(c);
                                }
                                for (Colour c : move2StudCharacterMessage.getStudFromBoard()) {
                                    game.getTurnHandler().checkProfessor(c);
                                }
                                useCharacter(move2StudCharacterMessage.getCharacterID());
                            }else{
                                notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.StudentError));
                            }
                        }else{
                            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.WrongStudentNumber));
                        }
                    } else {
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                    }
                }else{
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                }
            } else {
                notify(new ErrorMessage(move2StudCharacterMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }

    /**
     * Handles messages for using the character that removes three students of a given colour from tables
     * @param remove3StudCharacterMessage The message being handled
     */
    public void visit(Remove3StudCharacterMessage remove3StudCharacterMessage){
        if(game.isExpertMode()) {
            if (remove3StudCharacterMessage.getPlayerID() == game.getCurrentPlayer() && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(remove3StudCharacterMessage.getCharacterID()).getPrice()) {
                        for (Player p : game.getGameModel().getPlayers()) {
                            int removed = Math.min(3,p.getBoard().getTable(remove3StudCharacterMessage.getColour()));
                            for (int i = 0; i < removed; i++) {
                                try {
                                    p.getBoard().removeFromTable(remove3StudCharacterMessage.getColour());
                                } catch (StudentsOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        useCharacter(remove3StudCharacterMessage.getCharacterID());
                    } else {
                        notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NotEnoughCoinError));
                    }
                }else{
                    notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.CharacterAlreadyUsedError));
                }
            } else {
                notify(new ErrorMessage(remove3StudCharacterMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
            }
        }
        else {
            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.NormalModeError));
        }
    }
}
