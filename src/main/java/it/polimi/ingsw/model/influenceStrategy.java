package it.polimi.ingsw.model;
import java.util.HashMap;

/**
 * The interface for influence calculation strategy
 */
public interface influenceStrategy {
    /**
     * Calculates the influence of a particular player over the isle
     * @param p the player whose influence will be calculated
     * @param students the students on the isle
     * @param size the size of the isle
     * @param tower the faction currently owning the isle
     * @param professors the professors associated to who owns them
     * @return the influence of the player
     */
    int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour,Player> professors);
    /**
     * Calculates the influence of a particular team over the isle
     * @param t the team whose influence will be calculated
     * @param students the students on the isle
     * @param size the size of the isle
     * @param tower the faction currently owning the isle
     * @param professors the professors associated to who owns them
     * @return the influence of the team
     */
    int getInfluence(Team t, HashMap<Colour,Integer> students, int size, Faction tower,HashMap<Colour,Player> professors);
}