package it.polimi.ingsw.model;
import java.util.HashMap;

/**
 * Strategy for influence calculation used when towers are ignored
 */
public class noTowersStrategy implements influenceStrategy{

    /**
     * Calculates the influence of a particular player over the isle
     * @param p the player whose influence will be calculated
     * @param students the students on the isle
     * @param size the size of the isle
     * @param tower the faction currently owning the isle
     * @param professors the professors associated to who owns them
     * @return the influence of the player
     */
    @Override
    public int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour, Player> professors) {
        int influence = 0;
            for(Colour c: professors.keySet())
            {
                influence += (professors.get(c).equals(p))?students.get(c):0;
            }
        System.out.println(p.getNickname()+"  "+influence);
       return influence;
    }
    /**
     * Calculates the influence of a particular team over the isle
     * @param t the team whose influence will be calculated
     * @param students the students on the isle
     * @param size the size of the isle
     * @param tower the faction currently owning the isle
     * @param professors the professors associated to who owns them
     * @return the influence of the team
     */
    @Override
    public int getInfluence(Team t, HashMap<Colour, Integer> students, int size, Faction tower, HashMap<Colour, Player>professors) {
        int influence = 0;
        if(t.getFaction() == tower) {
            influence += getInfluence(t.getLeader(), students, size, tower,professors) + getInfluence(t.getMember(), students, size, tower,professors);
        }
        return influence;
    }
}
