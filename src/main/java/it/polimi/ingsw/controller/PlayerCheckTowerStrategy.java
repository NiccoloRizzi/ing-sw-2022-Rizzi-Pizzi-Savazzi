package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Optional;


public class PlayerCheckTowerStrategy implements CheckTowerStrategy {
    public void moveMn(GameModel gm, int island){
        Faction oldfaction;
        HashMap<Colour, Player> profs = gm.getProfessors();
        try {
            Isle current = gm.getIsle(island);
            if(!current.removeProhibited()) {
                Optional<Player> owner = gm.getPlayers().stream()
                        .reduce((p1, p2) -> (current.getInfluence(p1, profs) > current.getInfluence(p2, profs) ? p1 : p2));
                if (owner.isPresent() && !(owner.get().getBoard().getFaction() == current.getTower())) {
                    oldfaction=current.getTower();
                    owner.get().getBoard().useTowers(current.getSize());
                    current.setTower(owner.get().getBoard().getFaction());
                    for(Player p: gm.getPlayers()){
                        if(p.getBoard().getFaction()==oldfaction)
                            p.getBoard().addTowers(current.getSize());
                    }
                }
            }
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }

    }
}
