package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.stream.Collectors;


public class PlayerCheckTowerStrategy implements CheckTowerStrategy {
    public void checkTower(GameModel gm, int island){
        Faction oldfaction;
        HashMap<Colour, Player> profs = gm.getProfessors();
        try {
            Isle current = gm.getIsle(island);
            if(!current.removeProhibited()) {
                Optional<Player> owner = Optional.empty();
                List<Player> maxPlayers = gm.getPlayers().stream()
                        .filter((p) -> current.getInfluence(p, profs) == gm.getPlayers().stream()
                                .map(p1 -> current.getInfluence(p1, profs))
                                .max(Integer::compare)
                                .get())
                        .collect(Collectors.toList());
                if(maxPlayers.size() == 1) owner = Optional.of(maxPlayers.get(0));
//                Optional<Player> owner = gm.getPlayers().stream()
//                        .reduce((p1, p2) -> (current.getInfluence(p1, profs) > current.getInfluence(p2, profs) ? p1 : p2));
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
