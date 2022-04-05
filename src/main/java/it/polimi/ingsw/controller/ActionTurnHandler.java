package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;

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


}
