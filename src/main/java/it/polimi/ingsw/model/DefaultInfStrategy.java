package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The default strategy used to calculate influence
 */
public class DefaultInfStrategy implements influenceStrategy{
    /**
     * Calculates the influence of a particular player over the isle
     * @param player the player whose influence will be calculated
     * @param students the students on the isle
     * @param size the size of the isle
     * @param tower the faction currently owning the isle
     * @param professors the professors associated to who owns them
     * @return the influence of the player
     */
    @Override
    public int getInfluence(Player player, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour,Player> professors){
        int influence = 0;

        for(Colour c: professors.keySet())
        {
            influence += (player.equals(professors.get(c)))?students.get(c):0;

        }

        if(player.getBoard().getFaction() == tower)
            influence += size;
        return influence;
    }
    /**
     * Calculates the influence of a particular player over the isle
     * @param team the team whose influence will be calculated
     * @param students the students on the isle
     * @param size the size of the isle
     * @param tower the faction currently owning the isle
     * @param professors the professors associated to who owns them
     * @return the influence of the team
     */
    @Override
    public int getInfluence(Team team, HashMap<Colour, Integer> students, int size, Faction tower, HashMap<Colour,Player> professors)
    {
        return getInfluence(team.getLeader(),students,size,tower,professors)+ getInfluence(team.getMember(),students,size,tower,professors)-size;
    }
}
