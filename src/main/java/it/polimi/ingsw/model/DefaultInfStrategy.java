package it.polimi.ingsw.model;

import java.util.ArrayList;

public class DefaultInfStrategy implements influenceStrategy{
    public int getInfluence(Player p, ArrayList<Student> students, int size, Faction tower){
        int influence = 0;

        influence+= students.stream()
                            .filter(student -> p.getBoard().getProfessors()[student.getType().ordinal()])
                            .count();

        if(p.getBoard().getFaction() == tower)
            influence += size;
        return influence;
    }

    public int getInfluence(Team t, ArrayList<Student> students, int size, Faction tower)
    {
        return getInfluence(t.getLeader(),students,size,tower)+ getInfluence(t.getMember(),students,size,tower)-size;
    }

}
