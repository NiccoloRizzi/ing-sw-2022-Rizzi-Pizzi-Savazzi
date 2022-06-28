package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.exceptions.TowerOutOfBoundException;
import it.polimi.ingsw.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Strategy used when playing in teams
 */
public class TeamCheckTowerStrategy implements CheckTowerStrategy {
    /**
     * Determines which team should place a tower on the isle
     * @param gm The gamemodel
     * @param island The island being checked
     */
    @Override
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

            }else{
                gm.addProhibited();
            }
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        } catch (TowerOutOfBoundException e) {
            e.printStackTrace();
        }

    }
}
