package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.GameModel;

public class MessageVisitor {

    final Game game;

    public MessageVisitor(Game game){
        this.game = game;
    }

    public void visit(AssistantChoiceMessage assistantChoiceMessage){
    }
    public void visit(MoveStudentMessage moveStudentMessage){
    }
    public void visit(MoveMotherNatureMessage moveMotherNatureMessage){
    }
    public void visit(CloudChoiceMessage cloudChoiceMessage){
    }
    public void visit(IsleInfluenceCharacterMessage isleInfluenceCharacterMessage){
    }
    public void visit(MoveStudentCharacterMessage moveStudentCharacterMessage){
    }
    public void visit(StrategyProfessorMessage strategyProfessorMessage){
    }
    public void visit(SimilMotherNatureMesage similMotherNatureMesage){
    }
    public void visit(Plus2MoveMnMessage plus2MoveMnMessage){
        String answer;
        if(plus2MoveMnMessage.getPlayerId() == game.getCurrentPlayer()) {
            game.getGameModel().getPlayer(game.getCurrentPlayer()).getChosen().Boost();
        }
        else
        {
            answer = "Not your turn";
        }
    }
    public void visit(ProhibitedIsleCharacterMessage prohibitedIsleCharacterMessage){
        String answer;
        if(prohibitedIsleCharacterMessage.getPlayerId() == game.getCurrentPlayer()) {
            try{
                game.getGameModel().getIsle(prohibitedIsleCharacterMessage.getIsleIndex()).setProhibited();
            }catch(TileOutOfBoundsException e){
                answer = "Isle doesn't exist";
            }
            game.getGameModel().getCharacter(prohibitedIsleCharacterMessage.getCharId()).use();
        }
        else
        {
            answer = "Not your turn";
        }
    }
    public void visit(Move6StudCharacterMessage move6StudCharacterMessage){
        String answer;
        if(move6StudCharacterMessage.getPlayerId() == game.getCurrentPlayer())
        {
            CharacterStudents character= (CharacterStudents) game.getGameModel().getCharacter(move6StudCharacterMessage.getCharId());
            Board board =  game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
            for(Colour c: move6StudCharacterMessage.getStuds())
            {
                try {
                    board.removeStudent(c);
                }catch(StudentsOutOfBoundsException e){
                    answer="student not valid";
                }
                character.addStudent(c);
            }
            for(Colour c: move6StudCharacterMessage.getStuds())
            {
                try {
                    board.addToEntrance(c);
                }catch (StudentsOutOfBoundsException e){
                    answer = "entrance is full";
                }
                try {
                    character.removeStudent(c);
                }catch (StudentsOutOfBoundsException e){
                    answer = "character is empty";
                }
            }
            game.getGameModel().getCharacter(move6StudCharacterMessage.getCharId()).use();
        }
        else
        {
            answer = "Not your turn";
        }
    }
    public void visit(Move2StudCharacterMessage move2StudCharacterMessage){
        String answer;
        if(move2StudCharacterMessage.getPlayerId() == game.getCurrentPlayer())
        {
            Board board =  game.getGameModel().getPlayer(game.getCurrentPlayer()).getBoard();
            for(Colour c: move2StudCharacterMessage.getStud())
            {
                try {
                    board.removeStudent(c);
                }catch(StudentsOutOfBoundsException e){
                    answer="student not valid";
                }
                try {
                    board.addToTable(c);
                }catch(StudentsOutOfBoundsException e){
                    answer="table is full";
                }

            }
            for(Colour c: move2StudCharacterMessage.getStud_2())
            {
                try {
                    board.addToEntrance(c);
                }catch (StudentsOutOfBoundsException e){
                    answer = "entrance is full";
                }
                try {
                    board.removeFromTable(c);
                }catch (StudentsOutOfBoundsException e){
                    answer = "table is empty";
                }
                game.getGameModel().getCharacter(move2StudCharacterMessage.getCharId()).use();
            }
        }
        else
        {
            answer = "Not your turn";
        }
    }
    public void visit(Remove3StudCharacterMessage remove3StudCharacterMessage){
        String answer;
        if(remove3StudCharacterMessage.getPlayerId() == game.getCurrentPlayer())
        {
            for(Player p: game.getGameModel().getPlayers())
            {
                for(int i = 0; i <3 ;i++)
                {
                    try{
                        p.getBoard().removeFromTable(remove3StudCharacterMessage.getColour());
                    }catch(StudentsOutOfBoundsException e){}
                }
            }
            answer = "Player" +game.getGameModel().getPlayer(game.getCurrentPlayer()).getNickname() + "used character to remove 3 student";
        }
        else
        {
            answer = "Not your turn";
        }
    }
}
