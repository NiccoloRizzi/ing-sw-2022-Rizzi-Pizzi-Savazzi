package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Optional;

public class TeamCheckTowerStrategy implements CheckTowerStrategy {
    public void checkTower(GameModel gm, int island){
        HashMap<Colour, Player> profs = gm.getProfessors();
        Faction oldfaction;
        try {
            Isle current = gm.getIsle(island);
            if(!current.removeProhibited()) {
                Optional<Team> owners = gm.getTeams().stream()
                        .reduce((t1,t2)->current.getInfluence(t1,profs)>current.getInfluence(t2,profs)?t1:t2);
                if(owners.isPresent() && !(owners.get().getFaction()==current.getTower())){
                    int ownersIndex = gm.getTeams().indexOf(owners.get());
                    Team otherTeam = gm.getTeam((ownersIndex+1)%2);
                    oldfaction = current.getTower();
                    owners.get().useTowers(current.getSize());
                    current.setTower(owners.get().getFaction());
                    if(otherTeam.getFaction()==oldfaction)
                        otherTeam.addTowers(current.getSize());
                }

            }
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }

    }
}
