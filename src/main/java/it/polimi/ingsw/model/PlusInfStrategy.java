package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class PlusInfStrategy implements influenceStrategy{

    private int userID;

    public PlusInfStrategy(int userID)
    {
        this.userID = userID;
    }
    public int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour, Player> professors) {
        int influence = 0;
        if (tower.equals(p.getBoard().getFaction())) {
            influence += size;
        }
        for(Colour c: professors.keySet())
        {
            influence += (professors.get(c).equals(p))?students.get(c):0;
        }

        return (userID == p.getID())?influence + 2:influence ;
    }

    public int getInfluence(Team t, HashMap<Colour, Integer> students, int size, Faction tower, HashMap<Colour, Player> professors){
        return getInfluence(t.getLeader(), students, size, tower, professors)+ getInfluence(t.getMember(), students, size, tower, professors)-size;
    }
}
