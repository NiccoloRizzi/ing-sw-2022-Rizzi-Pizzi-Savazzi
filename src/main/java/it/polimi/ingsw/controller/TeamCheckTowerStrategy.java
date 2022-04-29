package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TeamCheckTowerStrategy implements CheckTowerStrategy {
    public void checkTower(GameModel gm, int island){
        HashMap<Colour, Player> profs = gm.getProfessors();
        Faction oldfaction;
        try {
            Isle current = gm.getIsle(island);
            if(!current.removeProhibited()) {
                Optional<Team> owners = Optional.empty();
                List<Team> maxTeams = gm.getTeams().stream()
                        .filter((t) -> current.getInfluence(t, profs) == gm.getTeams().stream()
                                .map(t1 -> current.getInfluence(t1, profs))
                                .max(Integer::compare)
                                .get())
                        .collect(Collectors.toList());
                if(maxTeams.size() == 1) owners = Optional.of(maxTeams.get(0));
//                Optional<Team> owners = gm.getTeams().stream()
//                        .reduce((t1,t2)->current.getInfluence(t1,profs)>current.getInfluence(t2,profs)?t1:t2);
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
