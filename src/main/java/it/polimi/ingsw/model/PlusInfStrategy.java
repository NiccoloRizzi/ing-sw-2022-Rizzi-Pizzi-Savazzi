package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class PlusInfStrategy implements influenceStrategy{

    public int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour, Player> professors) {
        int influence = 0;
        if (tower.equals(p.getBoard().getFaction())) {
            influence += size;
        }
        for(Colour c: Colour.values())
        {
            influence += students.get(c);
        }

        return influence + 2;
    }

    public int getInfluence(Team t, HashMap<Colour, Integer> students, int size, Faction tower, HashMap<Colour, Player> professors){
        return getInfluence(t.getLeader(), students, size, tower, professors)+ getInfluence(t.getMember(), students, size, tower, professors)-2-size;
    }
}
