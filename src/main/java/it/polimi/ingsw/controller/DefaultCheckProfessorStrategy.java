package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

import java.util.Optional;

/**
 * Default strategy used to determine who owns a professor
 */
public class DefaultCheckProfessorStrategy implements CheckProfessorStrategy {

    /**
     * Checks who should be the owner of a professor of a given colour
     * @param gm The gameModel
     * @param type The colour to check
     * @param currentPlayer Current player who causes the check to be performed
     */
    @Override
    public void checkProfessor(GameModel gm, Colour type, int currentPlayer){
        Optional<Player> currentOwner = gm.getProfessorOwner(type);
        Player player = gm.getPlayers().get(currentPlayer);
        if(currentOwner.isPresent()) {
            if (!player.equals(currentOwner.get()) && player.getBoard().getTable(type) > currentOwner.get().getBoard().getTable(type)) {
                gm.setProfessor(type, player);
            }
        }
        else{
            gm.setProfessor(type,player);
        }
    }
}
