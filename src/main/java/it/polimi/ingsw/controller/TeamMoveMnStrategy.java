package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.Optional;

public class TeamMoveMnStrategy implements MoveMnStrategy{
    public void moveMn(GameModel gm, int moves){
        gm.moveMN(moves);
        HashMap<Colour, Player> profs = gm.getProfessors();
        try {
            Isle current = gm.getIsle(gm.getMotherNature());
            if(current.removeProhibited()) {
                Optional<Team> owners = gm.getTeams().stream()
                        .reduce((t1,t2)->current.getInfluence(t1,profs)>current.getInfluence(t2,profs)?t1:t2);
                if(owners.isPresent() && !(owners.get().getFaction()==current.getTower())){
                    int ownersIndex = gm.getTeams().indexOf(owners.get());
                    owners.get().useTower();
                    gm.getTeam((ownersIndex+1)%2).addTower();
                }
            }
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }

    }
}
