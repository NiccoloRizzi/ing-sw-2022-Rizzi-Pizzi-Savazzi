package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultInfStrategy implements influenceStrategy{
    public int getInfluence(Player p, ArrayList<Student> students, int size, Faction tower, HashMap<Colour,Player> professors){
        int influence = 0;

        influence+= students.stream()
                            .filter(student -> professors.containsKey(student.getType())&&professors.get(student.getType()).equals(p))
                            .count();

        if(p.getBoard().getFaction() == tower)
            influence += size;
        return influence;
    }

    public int getInfluence(Team t, ArrayList<Student> students, int size, Faction tower,HashMap<Colour,Player> professors)
    {
        return getInfluence(t.getLeader(),students,size,tower,professors)+ getInfluence(t.getMember(),students,size,tower,professors)-size;
    }

}
