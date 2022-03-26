package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.HashMap;

public interface influenceStrategy {
    int getInfluence(Player p, ArrayList<Student> students, int size, Faction tower, HashMap<Colour,Player> professors);
    int getInfluence(Team t, ArrayList<Student> students, int size, Faction tower,HashMap<Colour,Player> professors);
}


