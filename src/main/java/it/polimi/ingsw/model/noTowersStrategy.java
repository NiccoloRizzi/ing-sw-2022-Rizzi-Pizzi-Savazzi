package it.polimi.ingsw.model;

import java.util.ArrayList;

public class noTowersStrategy implements influenceStrategy{
    @Override
    public int getInfluence(Player p, ArrayList<Student> students, int size, Faction tower) {
//        int influence = 0;
        return (int) students.
                stream().
                filter(s -> p.getBoard().getProfessors()[s.getType().ordinal()]).
                count();

//        if(p.getBoard().getFaction() == tower){
//            for(Student s : students){
//                if(p.getBoard().getProfessors()[s.getType().ordinal()]){
//                    influence++;
//                }
//            }
//        }
//        return influence;
    }

    @Override
    public int getInfluence(Team t, ArrayList<Student> students, int size, Faction tower) {
        int influence = 0;
        if(t.getFaction() == tower) {
            influence += getInfluence(t.getLeader(), students, size, tower) + getInfluence(t.getMember(), students, size, tower);
        }
        return influence;
    }
}
