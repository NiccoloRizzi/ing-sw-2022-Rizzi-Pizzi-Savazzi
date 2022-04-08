package it.polimi.ingsw.model;
import java.util.HashMap;

public interface influenceStrategy {
    int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour,Player> professors);
    int getInfluence(Team t, HashMap<Colour,Integer> students, int size, Faction tower,HashMap<Colour,Player> professors);
}


