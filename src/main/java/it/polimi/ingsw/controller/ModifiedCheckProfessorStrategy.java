package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.Player;

import java.util.Optional;

public class ModifiedCheckProfessorStrategy implements CheckProfessorStrategy {
    public void checkProfessor(GameModel gm, Colour type, int currentPlayer){
        Optional<Player> currentOwner = gm.getProfessorOwner(type);
        Player player = gm.getPlayers().get(currentPlayer);
        if(currentOwner.isPresent()) {
            if (!player.equals(currentOwner.get()) && player.getBoard().getTable(type) >= currentOwner.get().getBoard().getTable(type)) {
                gm.setProfessor(type, player);
            }
            else{
                gm.setProfessor(type,player);
            }
        }
    }

}
