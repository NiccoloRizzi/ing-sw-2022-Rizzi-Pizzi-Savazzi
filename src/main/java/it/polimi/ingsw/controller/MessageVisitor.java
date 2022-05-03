package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.messages.Answers.ErrorMessage;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;


import java.util.ArrayList;

public class MessageVisitor {

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
                if (player.getDeck().size() > 1) {
                    if(game.alreadyUsed(id)){
                        String answer = ErrorMessage.AssistantOtherPlayerError;
                    }
                    else{
                        player.setChoosenAssistant(id);
                        game.nextPlayer();
                    }
                }
            }
            else{
                String answer = ErrorMessage.AssistantAlreadyChosenError;
            }

        }else{
            String answer = ErrorMessage.TurnError;
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
            }
    }
    public void visit(MoveMotherNatureMessage moveMotherNatureMessage){
        if(moveMotherNatureMessage.getPlayerID()==game.getCurrentPlayer() && game.getTurnHandler().getPhase()==Phase.MOTHERNATURE){
            game.getTurnHandler().moveMn(moveMotherNatureMessage.getMoves());
        }
        else{
            String answer = "Non è il tuo turno per spostare madre natura.";
        }
    }
    public void visit(CloudChoiceMessage cloudChoiceMessage){
        if(game.getCurrentPlayer()==cloudChoiceMessage.getPlayerId() && game.getTurnHandler().getPhase()==Phase.CLOUD){
            game.getTurnHandler().moveFromCloud(cloudChoiceMessage.getCloudID());
        }
        else{
            String answer = "Non è il tuo turno per scegliere la nuvola.";
        }
    }
    public void visit(IsleInfluenceCharacterMessage isleInfluenceCharacterMessage) {
        String answer;
        int isleId = isleInfluenceCharacterMessage.getIsleID();
        int playerId = isleInfluenceCharacterMessage.getPlayerID();
        int charId = isleInfluenceCharacterMessage.getCharacterID();
        Colour noColor = isleInfluenceCharacterMessage.getNoColour();
        if (game.isExpertMode())
        {
            if(!game.getTurnHandler().isUsedCharacter()){
                if (game.getCurrentPlayer() == playerId) {
                    Player player = game.getGameModel().getPlayer(playerId);
                    Character character = game.getGameModel().getCharacter(charId);
                    if(player.getCoins() >= character.getPrice()){
                        try {
                            Isle isle = game.getGameModel().getIsle(isleId);
                            switch (character.getCard()) {
                                case NO_TOWER_INFLUENCE:
                                    isle.setInfStrategy(new noTowersStrategy());
                                    break;
                                case PLUS_2_INFLUENCE:
                                    isle.setInfStrategy(new PlusInfStrategy());
                                    break;
                                case NO_COLOUR_INFLUENCE:
                                    isle.setInfStrategy(new NoColourStrategy(noColor));
                            }
                            useCharacter(charId);
                        } catch (TileOutOfBoundsException e) {
                            e.printStackTrace();
                            answer = "INDICE ISOLA ERRATO";
                        }
                    }else{
                        answer = "Non hai abbastanza monete";
                    }
                }
                else {
                    answer = "NON è IL TUO TURNO";
                }
            }else {
                answer = "Personaggià già usato";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(MoveStudentCharacterMessage moveStudentCharacterMessage){
        String answer;
        int playerId = moveStudentCharacterMessage.getPlayerID();
        int charId = moveStudentCharacterMessage.getCharacterID();
        Colour stud = moveStudentCharacterMessage.getStudent();
        int tileId = moveStudentCharacterMessage.getTileID();
        if(game.isExpertMode()) {
            if(!game.getTurnHandler().isUsedCharacter()){
                if (game.getCurrentPlayer() == playerId) {
                    CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(charId);
                    Player player = game.getGameModel().getPlayer(playerId);
                    if(player.getCoins() >= character.getPrice()){
                        try {
                            Isle isle = game.getGameModel().getIsle(tileId);
                            Board board = game.getGameModel().getPlayer(playerId).getBoard();
                            character.removeStudent(stud);
                            character.addStudent(game.getGameModel().extractRandomStudent());
                            switch (character.getCard()) {
                                case ONE_STUD_TO_ISLE:
                                    isle.addStudent(stud);
                                    break;
                                case ONE_STUD_TO_TABLES:
                                    board.addToTable(stud);
                                    game.getTurnHandler().checkProfessor(stud);
                            }
                            useCharacter(charId);
                        } catch (TileOutOfBoundsException e) {
                            e.printStackTrace();
                            answer = "INDICE ISOLA ERRATO";
                        } catch (StudentsOutOfBoundsException e) {
                            e.printStackTrace();
                            answer = "INDICE STUDENTE ERRATO";
                        }
                    }else{
                        answer = "Non hai abbastanza monete";
                    }

                } else {
                    answer = "NON è IL TUO TURNO";
                }
            }else{
                answer = "Personaggio già usato";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(StrategyProfessorMessage strategyProfessorMessage){
        String answer;
        int playerId = strategyProfessorMessage.getPlayerID();
        int charId = strategyProfessorMessage.getCharacterID();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId) {
                Character character = game.getGameModel().getCharacter(charId);
                Player player = game.getGameModel().getPlayer(playerId);
                if(player.getCoins() >= character.getPrice()){
                    ActionTurnHandler handler = game.getTurnHandler();
                    if(!handler.isUsedCharacter()){
                        handler.setProfessorStrategy(new ModifiedCheckProfessorStrategy());
                        useCharacter(charId);
                    }else{
                        answer = "Personaggio già usato";
                    }
                }else{
                    answer = "Non hai abbastanza monete";
                }
            } else {
                answer = "NON è IL TUO TURNO";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(SimilMotherNatureMesage similMotherNatureMesage){
        String answer;
        int playerId = similMotherNatureMesage.getPlayerID();
        int charId = similMotherNatureMesage.getCharacterID();
        int isleId = similMotherNatureMesage.getIsleID();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId) {
                ActionTurnHandler handler = game.getTurnHandler();
                if(!handler.isUsedCharacter()){
                    Character character = game.getGameModel().getCharacter(charId);
                    Player player = game.getGameModel().getPlayer(playerId);
                    if(player.getCoins() >= character.getPrice()){
                        handler.checkTower(isleId);
                        handler.checkIsleJoin(isleId);
                        useCharacter(charId);
                    }else{
                        answer = "Non hai abbastanza monete";
                    }
                }else{
                    answer = "Personaggio già usato";
                }
            } else {
                answer = "NON è IL TUO TURNO";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(Plus2MoveMnMessage plus2MoveMnMessage){
        String answer;
        if (game.isExpertMode()) {
            if (plus2MoveMnMessage.getPlayerID() == game.getCurrentPlayer()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(plus2MoveMnMessage.getCharacterID()).getPrice()) {
                        game.getGameModel().getPlayer(game.getCurrentPlayer()).getChosen().Boost();
                        useCharacter(plus2MoveMnMessage.getCharacterID());

                    } else {
                        answer = "Not enough coin";
                    }
                }else{
                    answer = "In this turn a character was already used";
                }
            } else {
                answer = "Not your turn";
            }
        }  else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(ProhibitedIsleCharacterMessage prohibitedIsleCharacterMessage){
        String answer;
        if(game.isExpertMode()) {
            if (prohibitedIsleCharacterMessage.getPlayerID() == game.getCurrentPlayer()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(prohibitedIsleCharacterMessage.getCharacterID()).getPrice()) {
                        if (game.getGameModel().getProhibited() > 0) {
                            try {
                                game.getGameModel().getIsle(prohibitedIsleCharacterMessage.getIsleID()).setProhibited();
                            } catch (TileOutOfBoundsException e) {
                                answer = "Isle doesn't exist";
                            }
                            game.getGameModel().useProhibited();
                            useCharacter(prohibitedIsleCharacterMessage.getCharacterID());
                            ;
                        } else {
                            answer = "All 4 prohibited tile already in use";
                        }
                    } else {
                        answer = "Not enough coins";
                    }
                }else{
                    answer = "In this turn a character was already used";
                }
            } else {
                answer = "Not your turn";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(Move6StudCharacterMessage move6StudCharacterMessage){
        String answer;
        if(game.isExpertMode()) {
            if (move6StudCharacterMessage.getPlayerID() == game.getCurrentPlayer()) {
                if(!game.getTurnHandler().isUsedCharacter()){
                if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(move6StudCharacterMessage.getCharacterID()).getPrice()) {
                    CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(move6StudCharacterMessage.getCharacterID());
                    Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
                    for (Colour c : move6StudCharacterMessage.getStudFromBoard()) {
                        try {
                            board.removeStudent(c);
                        } catch (StudentsOutOfBoundsException e) {
                            answer = "student not valid";
                        }
                        character.addStudent(c);
                    }
                    for (Colour c : move6StudCharacterMessage.getStudsFromChar()) {
                        try {
                            board.addToEntrance(c);
                        } catch (StudentsOutOfBoundsException e) {
                            answer = "entrance is full";
                        }
                        try {
                            character.removeStudent(c);
                        } catch (StudentsOutOfBoundsException e) {
                            answer = "character is empty";
                        }
                    }
                    useCharacter(move6StudCharacterMessage.getCharacterID());
                } else {
                    answer = "Not enough coins";
                }
            }else {
                answer = "In this turn a character was already used";
            }
            } else {
                answer = "Not your turn";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }

    public void visit(Move2StudCharacterMessage move2StudCharacterMessage){
        String answer;
        if(game.isExpertMode()) {
            if (move2StudCharacterMessage.getPlayerID() == game.getCurrentPlayer()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(move2StudCharacterMessage.getCharacterID()).getPrice()) {
                        Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
                        for (Colour c : move2StudCharacterMessage.getStudFromBoard()) {
                            try {
                                board.removeStudent(c);
                            } catch (StudentsOutOfBoundsException e) {
                                answer = "student not valid";
                            }
                            try {
                                board.addToTable(c);
                            } catch (StudentsOutOfBoundsException e) {
                                answer = "table is full";
                            }

                        }
                        for (Colour c : move2StudCharacterMessage.getStudFromTables()) {
                            try {
                                board.addToEntrance(c);
                            } catch (StudentsOutOfBoundsException e) {
                                answer = "entrance is full";
                            }
                            try {
                                board.removeFromTable(c);
                            } catch (StudentsOutOfBoundsException e) {
                                answer = "table is empty";
                            }
                        }
                        useCharacter(move2StudCharacterMessage.getCharacterID());
                        for (Colour c : move2StudCharacterMessage.getStudFromTables()) {
                            game.getTurnHandler().checkProfessor(c);
                        }
                        for (Colour c : move2StudCharacterMessage.getStudFromBoard()) {
                            game.getTurnHandler().checkProfessor(c);
                        }
                    } else {
                        answer = "Not enough coins";
                    }
                }else{
                    answer = "In this turn a character was already used";
                }
            } else {
                answer = "Not your turn";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(Remove3StudCharacterMessage remove3StudCharacterMessage){
        String answer;
        if(game.isExpertMode()) {
            if (remove3StudCharacterMessage.getPlayerID() == game.getCurrentPlayer()) {
                if(!game.getTurnHandler().isUsedCharacter()) {
                    if (game.getGameModel().getPlayer(game.getCurrentPlayer()).getCoins() >= game.getGameModel().getCharacter(remove3StudCharacterMessage.getCharacterID()).getPrice()) {
                        for (Player p : game.getGameModel().getPlayers()) {
                            for (int i = 0; i < 3; i++) {
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
                        answer = "Not enough coins";
                    }
                }else{
                    answer = "In this turn a character was already used";
                }
            } else {
                answer = "Not your turn";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
}
