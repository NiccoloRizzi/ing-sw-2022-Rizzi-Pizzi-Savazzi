package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

public class DefaultCheckProfessorStrategy implements CheckProfessorStrategy {
    public void checkProfessor(GameModel gm, Colour type, int currentPlayer){
        Player currentOwner = gm.getProfessor(type);
        Player current = gm.getPlayers().get(currentPlayer);
        if(!current.equals(currentOwner)&&current.getBoard().getTable(type)>currentOwner.getBoard().getTable(type)){
            gm.setProfessor(type, current);
        }
    }
}
