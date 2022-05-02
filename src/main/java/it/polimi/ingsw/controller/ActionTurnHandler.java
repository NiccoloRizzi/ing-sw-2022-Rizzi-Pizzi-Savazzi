package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.*;

import java.util.Optional;

public class ActionTurnHandler {
    private int currentPlayer;
    private GameModel gameModel;
    private int studentsToMove;
    private CheckProfessorStrategy professorStrategy;
    private CheckTowerStrategy checkTowerStrategy;
    private Phase phase;

    private boolean usedCharacter;

    public ActionTurnHandler(int currentPlayer,GameModel gameModel,int numOfPlayers){
        this.currentPlayer = currentPlayer;
        this.gameModel = gameModel;
        studentsToMove = 3;
        phase = Phase.STUDENTS;
        professorStrategy= new DefaultCheckProfessorStrategy();
        checkTowerStrategy = (numOfPlayers == 4)? new TeamCheckTowerStrategy() : new PlayerCheckTowerStrategy();
        usedCharacter = false;
    }

    public void moveMn(int moves){
        Assistant a = gameModel.getPlayers().get(currentPlayer).getChosen();
        Optional<String> answer = Optional.empty();
        if(moves<=a.getMn_moves()+a.getBoost() && moves>=0){
            gameModel.moveMN(moves);
            checkTower(gameModel.getMotherNature());
            checkIsleJoin(gameModel.getMotherNature());
            phase=Phase.CLOUD;
        }
        else{
            answer=Optional.of("The number of moves must be between 0 and "+a.getMn_moves()+a.getBoost()+"!");
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
                }
            }
            else {
                String answer = "Not enough students to move";
            }
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    public void moveStudentToTable(Colour student){
        Optional<String> answer = Optional.empty();
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
                }
                else{
                    answer = Optional.of("Il tavolo è pieno.");
                }
            }else{
                answer = Optional.of("Non hai abbastanza studenti.");
            }


        }catch(StudentsOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void moveFromCloud(int cloudId){
        Optional<String> answer=Optional.empty();
        try{
            if(gameModel.getCloud(cloudId).isEmpty()){
                answer= Optional.of("The cloud you chose has already been emptied.");
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
            if(gameModel.getIsle(index).getTower()!=Faction.Empty) {
                if (gameModel.getIsle(index).getTower() == gameModel.getIsle((index == 0) ? gameModel.getIsles().size() - 1 : index - 1).getTower()) {
                    gameModel.joinIsle((index == 0) ? gameModel.getIsles().size() - 1 : index - 1, index);
                    index = (index == 0) ? 0 : index - 1;
                }

                if (gameModel.getIsle(index).getTower() == gameModel.getIsle((index + 1) % gameModel.getIsles().size()).getTower()) {
                    gameModel.joinIsle(index, (index + 1) % gameModel.getIsles().size());
                }
            }
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
