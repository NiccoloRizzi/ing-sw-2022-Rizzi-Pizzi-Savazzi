package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class NoColourStrategy implements influenceStrategy{
    private Colour noColour;

    public NoColourStrategy(Colour noColour)
    {
        this.noColour=noColour;
    }
    public int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour,Player> professors){
        int influence = 0;

        for(Colour c: Colour.values())
        {
            influence += (c == noColour) ? 0 : students.get(c);
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
