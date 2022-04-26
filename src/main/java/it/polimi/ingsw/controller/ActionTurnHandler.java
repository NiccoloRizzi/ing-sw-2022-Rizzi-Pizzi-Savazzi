package it.polimi.ingsw.controller;

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
    private CheckTowerStrategy moveMNstrategy;
    private Phase phase;

    public ActionTurnHandler(int currentPlayer,GameModel gameModel,int numOfPlayers){
        this.currentPlayer = currentPlayer;
        this.gameModel = gameModel;
        professorStrategy= new DefaultCheckProfessorStrategy();
        moveMNstrategy = (numOfPlayers == 4)? new TeamCheckTowerStrategy() : new PlayerCheckTowerStrategy();

    }

    public void moveMn(int moves){
        Assistant a = gameModel.getPlayers().get(currentPlayer).getChosen();
        Optional<String> answer = Optional.empty();
        if(moves<=a.getMn_moves()+a.getBoost() && moves>=0){
            gameModel.moveMN(moves);
        }
        else{
            answer=Optional.of("The number of moves must be between 0 and "+a.getMn_moves()+a.getBoost()+"!");
        }
    }

    public void moveMnToIsle(int isleId){
        try {
            gameModel.setMotherNPos(isleId);
        } catch (TileOutOfBoundsException e) {
            e.printStackTrace();
            String answer = "ISLE INDEX OUT OF BOUND";
        }
    }



    public void moveStudentToIsle(Colour student, int isle){
        Player player = gameModel.getPlayer(currentPlayer);
        try{
            if(player.getBoard().getStudents(student)>0){
                if(isle < gameModel.getIsles().size()) {
                    player.getBoard().removeStudent(student);
                    gameModel.getIsle(isle).addStudent(student);
                }
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

    public void moveFromCloud(int cloudId, int playerId){
        Optional<String> answer=Optional.empty();
        try{
            if(studentsToMove>0){
                answer=Optional.of("You still have "+studentsToMove+" students you have to move from your entrance!");
            }
            else if(gameModel.getCloud(cloudId).isEmpty()){
                answer= Optional.of("The cloud you chose has already been emptied.");
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
            e.printStackTrace();
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

    public int getStudentsToMove() {
        return studentsToMove;
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

    public Phase getPhase(){
        return phase;
    }

    void setProfessorStrategy(CheckProfessorStrategy strategy)
    {
        professorStrategy = strategy;
    }
}
