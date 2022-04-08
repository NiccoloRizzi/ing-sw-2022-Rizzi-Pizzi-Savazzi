package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.*;

public class ActionTurnHandler {
    private int currentPlayer;
    private GameModel gameModel;
    private int studentsToMove;


    public void moveFromCloud(int cloudId, int playerId){
        String answer;
        if(cloudId < gameModel.getClouds().size() && cloudId>0){
            if(studentsToMove>0){
                answer="You still have "+studentsToMove+" students you have to move from your entrance!";
            }
            else if(gameModel.getCloud(cloudId).isEmpty()){
                answer="The cloud you chose has already been emptied.";
            }
            else{
                gameModel.getPlayer(playerId).getBoard().addStudents(gameModel.getCloud(cloudId).empty());
            }
        }
        else{
            answer="Illegal cloud chosen.";
        }
    }

    public void checkProfessors(Colour type)
    {
        if(!gameModel.getPlayer(currentPlayer).equals(gameModel.getProfessor(type))) {
            if(gameModel.getProfessor(type).getBoard().getTable(type) < gameModel.getPlayer(currentPlayer).getBoard().getTable(type))
                gameModel.setProfessor(type,gameModel.getPlayer(currentPlayer));
        }
    }

    public void checkIsleJoin(int index)
    {
        if(gameModel.getIsle(index).getTower() == gameModel.getIsle((index == 0)? gameModel.getIsles().size()-1: index -1).getTower())
        {
            gameModel.joinIsle(index,(index == 0)? gameModel.getIsles().size()-1: index -1);
            index--;
        }

        if(gameModel.getIsle(index).getTower() == gameModel.getIsle((index +1)%gameModel.getIsles().size()).getTower())
        {
            gameModel.joinIsle(index,(index +1)%gameModel.getIsles().size());
            index--;
        }
    }
    //type = 1 muove sull'isola mentre type = 0 muove sul tavolo
    public void moveStudent(int boardPosition, boolean type, int isleIndex)
    {
        Student temp = gameModel.getPlayer(currentPlayer).getBoard().getStudent(boardPosition);
        gameModel.getPlayer(currentPlayer).getBoard().removeStudent(boardPosition);
        if(type)
        {
            gameModel.getIsle(isleIndex).addStudent(temp);
        }
        else
        {
            try {
                gameModel.getPlayer(currentPlayer).getBoard().addToTable(temp.getType().ordinal());
            }catch (StudentsOutOfBoundsException e){
                //error message: tavolo pieno
            }
        }
    }
}
