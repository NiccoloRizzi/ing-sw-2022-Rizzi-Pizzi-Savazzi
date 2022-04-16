package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.*;

public class ActionTurnHandler {
    private int currentPlayer;
    private GameModel gameModel;
    private int studentsToMove;
    private CheckProfessorStrategy professorStrategy;
    private MoveMnStrategy moveMNstrategy;

    public ActionTurnHandler(int currentPlayer,GameModel gameModel,int numOfPlayers){
        this.currentPlayer = currentPlayer;
        this.gameModel = gameModel;
        professorStrategy= new DefaultCheckProfessorStrategy();
        moveMNstrategy = (numOfPlayers == 4)? new TeamMoveMnStrategy () : new PlayerMoveMnStrategy();

    }

    public void moveFromCloud(int cloudId, int playerId){
        String answer;
        try{
            if(studentsToMove>0){
                answer="You still have "+studentsToMove+" students you have to move from your entrance!";
            }
            else if(gameModel.getCloud(cloudId).isEmpty()){
                answer="The cloud you chose has already been emptied.";
            }
            else{
                try {
                    gameModel.getPlayer(playerId).getBoard().addStudents(gameModel.getCloud(cloudId).empty());
                }catch(TileOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        }
        catch(TileOutOfBoundsException e){
            answer="Illegal cloud chosen.";
        }
    }

    public void checkIsleJoin(int index)
    {
        try {
            if (gameModel.getIsle(index).getTower() == gameModel.getIsle((index == 0) ? gameModel.getIsles().size() - 1 : index - 1).getTower()) {
                gameModel.joinIsle(index, (index == 0) ? gameModel.getIsles().size() - 1 : index - 1);
                index--;
            }

            if (gameModel.getIsle(index).getTower() == gameModel.getIsle((index + 1) % gameModel.getIsles().size()).getTower()) {
                gameModel.joinIsle(index, (index + 1) % gameModel.getIsles().size());
            }
        }catch(TileOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }
    //type = 1 muove sull'isola mentre type = 0 muove sul tavolo
    public void moveStudent(Colour student, boolean type, int isleIndex) {
        if (studentsToMove > 0) {
            if (gameModel.getPlayer(currentPlayer).getBoard().getStudents(student) > 0) {
                try {
                    gameModel.getPlayer(currentPlayer).getBoard().removeStudent(student);
                }catch(StudentsOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
                if (type) {
                    try {
                        gameModel.getIsle(isleIndex).addStudent(student);
                        studentsToMove--;
                    } catch (TileOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        gameModel.getPlayer(currentPlayer).getBoard().addToTable(student);
                        professorStrategy.checkProfessor(gameModel,student,currentPlayer);
                        studentsToMove--;
                    } catch (StudentsOutOfBoundsException e) {
                        //error message: tavolo pieno
                    }
                }
            }
        } else {
            //messaggio errore mossa non consentita
        }
    }

    void setProfessorStrategy(CheckProfessorStrategy strategy)
    {
        professorStrategy = strategy;
    }
}
