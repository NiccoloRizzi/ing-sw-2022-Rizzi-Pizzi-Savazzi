package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Strategy for influence calculation used when a player has influence boost
 */
public class PlusInfStrategy implements influenceStrategy{

    /**
     * User ID of the player on whom the strategy will be applied
     */
    private int userID;

    public PlusInfStrategy(int userID)
    {
        this.userID = userID;
    }

    /**
     *
     * @param player The player whose influence will be calculated
     * @param students The students on the isle
     * @param size The size of the isle
     * @param tower The faction controlling the isle
     * @param professors HashMap linking professors to who owns them
     * @return The influence of the given player on an island
     */
    @Override
    public int getInfluence(Player player, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour, Player> professors) {
        int influence = 0;
        if (tower.equals(player.getBoard().getFaction())) {
            influence += size;
        }
        for(Colour c: professors.keySet())
        {
            influence += (professors.get(c).equals(player))?students.get(c):0;
        }

        return (userID == player.getID())?influence + 2:influence ;
    }

    /**
     *
     * @param team The team of which influence will be calculated
     * @param students The students on the isle
     * @param size The size of the isle
     * @param tower The faction controlling the isle
     * @param professors HashMap linking professors to who owns them
     * @return The influence of the given team over the isle
     */
    @Override
    public int getInfluence(Team team, HashMap<Colour, Integer> students, int size, Faction tower, HashMap<Colour, Player> professors){
        return getInfluence(team.getLeader(), students, size, tower, professors)+ getInfluence(team.getMember(), students, size, tower, professors)-size;
    }
}
