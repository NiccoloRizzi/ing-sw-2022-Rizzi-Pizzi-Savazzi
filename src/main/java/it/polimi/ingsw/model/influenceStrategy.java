package it.polimi.ingsw.model;
import java.util.ArrayList;

public interface influenceStrategy {
    int getInfluence(Player p, ArrayList<Student> students, int size);
    int getInfluence(Team t, ArrayList<Student> students, int size);
}


