package it.polimi.ingsw.model;
import java.util.ArrayList;

public interface influenceStrategy {
    int getInfluence(Player p, ArrayList<Student> students, int size, Faction tower);
    int getInfluence(Team t, ArrayList<Student> students, int size, Faction tower);
}


