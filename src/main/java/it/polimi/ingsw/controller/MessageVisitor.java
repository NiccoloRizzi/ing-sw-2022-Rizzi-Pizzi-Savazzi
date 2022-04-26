package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;


import java.util.ArrayList;

public class MessageVisitor {

    final Game game;
    public MessageVisitor(Game game){
        this.game = game;
    }

    public void visit(AssistantChoiceMessage assistantChoiceMessage){
        int playerID = game.getCurrentPlayer();
        if(assistantChoiceMessage.getPlayerId()==playerID && game.getTurnHandler().getPhase()==Phase.ASSISTANT) {
            ArrayList<Player> players = game.getGameModel().getPlayers();
            Player player = players.get(playerID);
            int id = assistantChoiceMessage.getAssistantId();
            if(player.getDeck().size()>1) {
                for (int i = 0; i < game.getPlayersOrder().indexOf(playerID); i++) {
                    if (players.get(game.getPlayersOrder().get(i)).getChosen().getValue() == id) {
                        String answer = "L'assistente è già stato scelto da un altro giocatore.";
                        break;
                    }
                }
            }
            if(!player.hasUsed(id)){
                player.setChoosenAssistant(id);
            }
            else{
                String answer = "Hai già usato questo assistente.";
            }
        }else{
            String answer= "Non è il tuo turno per scegliere l'assistente.";
        }

    }
    public void visit(MoveStudentMessage moveStudentMessage){
            int playerID = moveStudentMessage.getPlayerId();
            Colour student = moveStudentMessage.getStudentIndex();
            if (playerID == game.getCurrentPlayer() && game.getTurnHandler().getStudentsToMove()>0) {
                Player current = game.getGameModel().getPlayer(playerID);
                if (moveStudentMessage.isToTable()) {
                    game.getTurnHandler().moveStudentToTable(student);
                } else {
                    int isle = moveStudentMessage.getTileIndex();
                    game.getTurnHandler().moveStudentToIsle(student,isle);
                }
            }
    }
    public void visit(MoveMotherNatureMessage moveMotherNatureMessage){
        if(moveMotherNatureMessage.getPlayerId()==game.getCurrentPlayer() && game.getTurnHandler().getPhase()==Phase.MOTHERNATURE){
            game.getTurnHandler().moveMn(moveMotherNatureMessage.getMoves());
        }
        else{
            String answer = "Non è il tuo turno per spostare madre natura.";
        }
    }
    public void visit(CloudChoiceMessage cloudChoiceMessage){
        if(game.getCurrentPlayer()==cloudChoiceMessage.getPlayerId() && game.getTurnHandler().getPhase()==Phase.CLOUD){
            game.getTurnHandler().moveFromCloud(cloudChoiceMessage.getCloudIndex(), cloudChoiceMessage.getPlayerId());
        }
        else{
            String answer = "Non è il tuo turno per scegliere la nuvola.";
        }
    }
    public void visit(IsleInfluenceCharacterMessage isleInfluenceCharacterMessage) {
        String answer;
        int isleId = isleInfluenceCharacterMessage.getIsleIndex();
        int playerId = isleInfluenceCharacterMessage.getPlayerId();
        int charId = isleInfluenceCharacterMessage.getCharatcerId();
        Colour noColor = isleInfluenceCharacterMessage.getNoColour();
        if (game.isExpertMode())
        {
            if (game.getCurrentPlayer() == playerId) {
                try {
                    Character character = game.getGameModel().getCharacter(charId);
                    Isle isle = game.getGameModel().getIsle(isleId);
                    switch (CharactersEnum.values()[character.getId()]) {
                        case NO_TOWER_INFLUENCE:
                            isle.setInfStrategy(new noTowersStrategy());
                            break;
                        case PLUS_2_INFLUENCE:
                            isle.setInfStrategy(new PlusInfStrategy());
                            break;
                        case NO_COLOUR_INFLUENCE:
                            isle.setInfStrategy(new NoColourStrategy(noColor));
                    }
                    character.use();
                } catch (TileOutOfBoundsException e) {
                    e.printStackTrace();
                    answer = "INDICE ISOLA ERRATO";
                }
            } else {
                answer = "NON è IL TUO TURNO";
            }
        }
        else {
        answer = "Not available in normal mode";
        }
    }
    public void visit(MoveStudentCharacterMessage moveStudentCharacterMessage){
        String answer;
        int playerId = moveStudentCharacterMessage.getPlayerId();
        int charId = moveStudentCharacterMessage.getCharacterId();
        Colour stud = moveStudentCharacterMessage.getStudentIndex();
        int tileId = moveStudentCharacterMessage.getTileIndex();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId) {
                CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(charId);
                try {
                    Isle isle = game.getGameModel().getIsle(tileId);
                    Board board = game.getGameModel().getPlayer(playerId).getBoard();
                    character.removeStudent(stud);
                    character.addStudent(game.getGameModel().getRandomStudent());
                    switch (CharactersEnum.values()[character.getId()]) {
                        case ONE_STUD_TO_ISLE:
                            isle.addStudent(stud);
                            break;
                        case ONE_STUD_TO_TABLES:
                            board.addToTable(stud);
                    }
                    character.use();
                } catch (TileOutOfBoundsException e) {
                    e.printStackTrace();
                    answer = "INDICE ISOLA ERRATO";
                } catch (StudentsOutOfBoundsException e) {
                    e.printStackTrace();
                    answer = "INDICE STUDENTE ERRATO";
                }

            } else {
                answer = "NON è IL TUO TURNO";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
    public void visit(StrategyProfessorMessage strategyProfessorMessage){
        String answer;
        int playerId = strategyProfessorMessage.getPlayerId();
        int charId = strategyProfessorMessage.getCharacterId();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId) {
                Character character = game.getGameModel().getCharacter(charId);
                ActionTurnHandler handler = game.getTurnHandler();
                handler.setProfessorStrategy(new ModifiedCheckProfessorStrategy());
                character.use();
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
        int playerId = similMotherNatureMesage.getPlayerId();
        int charId = similMotherNatureMesage.getCharacterId();
        int isleId = similMotherNatureMesage.getIsleIndex();
        if(game.isExpertMode()) {
            if (game.getCurrentPlayer() == playerId) {
                ActionTurnHandler handler = game.getTurnHandler();
                Character character = game.getGameModel().getCharacter(charId);
                handler.moveMnToIsle(isleId);
                handler.checkIsleJoin(isleId);
                character.use();
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
            if (plus2MoveMnMessage.getPlayerId() == game.getCurrentPlayer()) {
                game.getGameModel().getPlayer(game.getCurrentPlayer()).getChosen().Boost();
                game.getGameModel().getCharacter(plus2MoveMnMessage.getCharacterId()).use();
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
            if (prohibitedIsleCharacterMessage.getPlayerId() == game.getCurrentPlayer()) {
                try {
                    game.getGameModel().getIsle(prohibitedIsleCharacterMessage.getIsleIndex()).setProhibited();
                } catch (TileOutOfBoundsException e) {
                    answer = "Isle doesn't exist";
                }
                game.getGameModel().getCharacter(prohibitedIsleCharacterMessage.getCharId()).use();
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
            if (move6StudCharacterMessage.getPlayerId() == game.getCurrentPlayer()) {
                CharacterStudents character = (CharacterStudents) game.getGameModel().getCharacter(move6StudCharacterMessage.getCharId());
                Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
                for (Colour c : move6StudCharacterMessage.getStuds()) {
                    try {
                        board.removeStudent(c);
                    } catch (StudentsOutOfBoundsException e) {
                        answer = "student not valid";
                    }
                    character.addStudent(c);
                }
                for (Colour c : move6StudCharacterMessage.getStuds()) {
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
                game.getGameModel().getCharacter(move6StudCharacterMessage.getCharId()).use();
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
            if (move2StudCharacterMessage.getPlayerId() == game.getCurrentPlayer()) {
                Board board = game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
                for (Colour c : move2StudCharacterMessage.getStud()) {
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
                for (Colour c : move2StudCharacterMessage.getStud_2()) {
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
                    game.getGameModel().getCharacter(move2StudCharacterMessage.getCharId()).use();
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
            if (remove3StudCharacterMessage.getPlayerId() == game.getCurrentPlayer()) {
                for (Player p : game.getGameModel().getPlayers()) {
                    for (int i = 0; i < 3; i++) {
                        try {
                            p.getBoard().removeFromTable(remove3StudCharacterMessage.getColour());
                        } catch (StudentsOutOfBoundsException e) {
                        }
                    }
                }
                game.getGameModel().getCharacter(remove3StudCharacterMessage.getCharId()).use();
                answer = "Player" + game.getGameModel().getPlayer(game.getCurrentPlayer()).getNickname() + "used character to remove 3 student";
            } else {
                answer = "Not your turn";
            }
        }
        else {
            answer = "Not available in normal mode";
        }
    }
}
