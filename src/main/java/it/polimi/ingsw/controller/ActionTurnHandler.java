package it.polimi.ingsw.controller;

import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

import java.util.List;
import java.util.Optional;

public class ActionTurnHandler extends Observable<ClientModel> {
    private int currentPlayer;
    private GameModel gameModel;
    private int studentsToMove;
    private CheckProfessorStrategy professorStrategy;
    private CheckTowerStrategy checkTowerStrategy;
    private Phase phase;

    private boolean usedCharacter;

    public ActionTurnHandler(GameModel gameModel){
        this.gameModel = gameModel;

    }

    public void setupActionTurnHandler(int currentPlayer,int numOfPlayers){
        this.currentPlayer = currentPlayer;
        studentsToMove = 3;
        phase = Phase.STUDENTS;
        professorStrategy= new DefaultCheckProfessorStrategy();
        checkTowerStrategy = (numOfPlayers == 4)? new TeamCheckTowerStrategy() : new PlayerCheckTowerStrategy();
        usedCharacter = false;
    }

    public void moveMn(int moves){
        Assistant a = gameModel.getPlayers().get(currentPlayer).getChosen();

        if(moves<=a.getMn_moves()+a.getBoost() && moves>=0){
            gameModel.moveMN(moves);
            checkTower(gameModel.getMotherNature());
            checkIsleJoin(gameModel.getMotherNature());
            phase=Phase.CLOUD;
            notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_CLOUDS));
        }
        else{
            notify(new ErrorMessage(ErrorMessage.ErrorType.MovesError));
        }
    }

    public void moveStudentToIsle(Colour student, int isle){
        Player player = gameModel.getPlayer(currentPlayer);
        try{
            if(player.getBoard().getStudents(student)>0){
                if(isle < gameModel.getIsles().size()) {
                    player.getBoard().removeStudent(student);
                    gameModel.getIsle(isle).addStudent(student);
                    studentsToMove --;
                    if(studentsToMove  == 0)
                        phase = Phase.MOTHERNATURE;
                        notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_MN));
                }
            }
            else {
                notify(new ErrorMessage(ErrorMessage.ErrorType.StudentError));
            }
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void moveStudentToTable(Colour student){
        try{
            Player player = gameModel.getPlayer(currentPlayer);
            if(player.getBoard().getStudents(student)>0) {
                if(!player.getBoard().isTableFull(student)) {
                    player.getBoard().removeStudent(student);
                    player.getBoard().addToTable(student);
                    checkProfessor(student);
                    studentsToMove --;
                    if(studentsToMove  == 0)
                        phase = Phase.MOTHERNATURE;
                    notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_MN));
                }
                else{
                    notify(new ErrorMessage(ErrorMessage.ErrorType.TileIsFullError));
                }
            }else{
                notify(new ErrorMessage(ErrorMessage.ErrorType.StudentError));
            }


        }catch(StudentsOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void moveFromCloud(int cloudId){

        try{
            if(gameModel.getCloud(cloudId).isEmpty()){
                notify(new ErrorMessage(ErrorMessage.ErrorType.CloudError));
            }
            else{
                try {
                    gameModel.getPlayer(currentPlayer).getBoard().addStudents(gameModel.getCloud(cloudId).empty());
                }catch(TileOutOfBoundsException e){
                    e.printStackTrace();
                }
            }
        }
        catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void checkIsleJoin(int index)
    {
        try {
            gameModel.joinIsle(index);
        }catch(TileOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }

    public int getStudentsToMove() {
        return studentsToMove;
    }
    public Phase getPhase(){
        return phase;
    }

     public void setProfessorStrategy(CheckProfessorStrategy strategy)
    {
        professorStrategy = strategy;
    }
    public void checkProfessor(Colour student)
    {
        professorStrategy.checkProfessor(gameModel,student,currentPlayer);
    }

    public void checkTower(int isle){
        checkTowerStrategy.checkTower(gameModel,isle);
    }
    public void setCurrentPlayer(Player player)
    {
        this.currentPlayer = player.getID();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean isUsedCharacter() {
        return usedCharacter;
    }
    public void setUsedCharacter (boolean used)
    {
        usedCharacter = used;
    }
}
