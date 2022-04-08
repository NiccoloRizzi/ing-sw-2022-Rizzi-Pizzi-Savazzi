package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultInfStrategy implements influenceStrategy{
    public int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour,Player> professors){
        int influence = 0;

        for(Colour c: Colour.values())
        {
            influence += (p.equals(professors.get(c)))?students.get(c):0;

        }

        if(p.getBoard().getFaction() == tower)
            influence += size;
        return influence;
    }

    public int getInfluence(Team t, HashMap<Colour, Integer> students, int size, Faction tower, HashMap<Colour,Player> professors)
    {
        return getInfluence(t.getLeader(),students,size,tower,professors)+ getInfluence(t.getMember(),students,size,tower,professors)-size;
    }
}
