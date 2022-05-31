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

public class MessageVisitor extends Observable<ClientModel> {

    final Game game;
    public MessageVisitor(Game game){
        this.game = game;
    }

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
    public void visit(MoveStudentMessage moveStudentMessage){
            int playerID = moveStudentMessage.getPlayerID();
            Colour student = moveStudentMessage.getStudent();
            if (playerID == game.getCurrentPlayer() && game.getTurnHandler().getStudentsToMove()>0) {
                Player current = game.getGameModel().getPlayer(playerID);
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
    public void visit(MoveMotherNatureMessage moveMotherNatureMessage){
        if(moveMotherNatureMessage.getPlayerID()==game.getCurrentPlayer() && game.getTurnHandler().getPhase()==Phase.MOTHERNATURE){
            game.getTurnHandler().moveMn(moveMotherNatureMessage.getMoves());
            game.checkEndTowerIsle();
        }
        else{
            notify(new ErrorMessage(moveMotherNatureMessage.getPlayerID(), ErrorMessage.ErrorType.NotYourTurnError));
        }
    }
    public void visit(CloudChoiceMessage cloudChoiceMessage){
        if(game.getCurrentPlayer()==cloudChoiceMessage.getPlayerId() && game.getTurnHandler().getPhase()==Phase.CLOUD){
            if(game.getTurnHandler().moveFromCloud(cloudChoiceMessage.getCloudID()))
            game.nextPlayer();
        }
        else{
            notify(new ErrorMessage(cloudChoiceMessage.getPlayerId(), ErrorMessage.ErrorType.NotYourTurnError));
        }
    }
    public void visit(IsleInfluenceCharacterMessage isleInfluenceCharacterMessage) {
        String answer;
        int playerId = isleInfluenceCharacterMessage.getPlayerID();
        int charId = isleInfluenceCharacterMessage.getCharacterID();
        Colour noColor = isleInfluenceCharacterMessage.getNoColour();
        if (game.isExpertMode())
        {
            if (game.getCurrentPlayer() == playerId && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()){

                    Player player = game.getGameModel().getPlayer(playerId);
                    Character character = game.getGameModel().getCharacter(charId);
                    if(player.getCoins() >= character.getPrice()){
                            switch (character.getCard()) {
                                case NO_TOWER_INFLUENCE:
                                    game.getTurnHandler().setInfStrategy(new noTowersStrategy());
                                    break;
                                case PLUS_2_INFLUENCE:
                                    game.getTurnHandler().setInfStrategy(new PlusInfStrategy(playerId));
                                    break;
                                case NO_COLOUR_INFLUENCE:
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
    public void visit(MoveStudentCharacterMessage moveStudentCharacterMessage){
        String answer;
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
                        try {
                            Board board = game.getGameModel().getPlayer(playerId).getBoard();
                            character.removeStudent(stud);
                            character.addStudent(game.getGameModel().extractRandomStudent());
                            switch (character.getCard()) {
                                case ONE_STUD_TO_ISLE:
                                    Isle isle = game.getGameModel().getIsle(tileId);
                                    isle.addStudent(stud);
                                    break;
                                case ONE_STUD_TO_TABLES:
                                    board.addToTable(stud);
                                    game.getTurnHandler().checkProfessor(stud);
                            }
                            useCharacter(charId);
                        } catch (TileOutOfBoundsException e) {
                            e.printStackTrace();
                            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.IsleError));
                        } catch (StudentsOutOfBoundsException e) {
                            e.printStackTrace();
                            notify(new ErrorMessage(game.getCurrentPlayer(), ErrorMessage.ErrorType.StudentError));
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
    public void visit(StrategyProfessorMessage strategyProfessorMessage){
        String answer;
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
    public void visit(SimilMotherNatureMesage similMotherNatureMesage){
        String answer;
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
    public void visit(Plus2MoveMnMessage plus2MoveMnMessage){
        String answer;
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
    public void visit(ProhibitedIsleCharacterMessage prohibitedIsleCharacterMessage){
        String answer;
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
    public void visit(Move6StudCharacterMessage move6StudCharacterMessage){
        String answer;
        if(game.isExpertMode()) {
            if (move6StudCharacterMessage.getPlayerID() == game.getCurrentPlayer() && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()){
                if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(move6StudCharacterMessage.getCharacterID()).getPrice()) {
                    CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(move6StudCharacterMessage.getCharacterID());
                    Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
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

    public void visit(Move2StudCharacterMessage move2StudCharacterMessage){
        String answer;
        if(game.isExpertMode()) {
            if (move2StudCharacterMessage.getPlayerID() == game.getCurrentPlayer() && !game.isPlanning()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(move2StudCharacterMessage.getCharacterID()).getPrice()) {
                        Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
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
    public void visit(Remove3StudCharacterMessage remove3StudCharacterMessage){
        String answer;
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
                        answer = "Player" + game.getGameModel().getPlayer(game.getCurrentPlayer()).getNickname() + "used character to remove 3 student";
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
