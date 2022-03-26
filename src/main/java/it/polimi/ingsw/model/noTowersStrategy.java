package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class noTowersStrategy implements influenceStrategy{
    @Override
    public int getInfluence(Player p, ArrayList<Student> students, int size, Faction tower, HashMap<Colour, Player> professors) {
//        int influence = 0;
        return (int) students.
                stream()
                .filter(student -> professors.get((student.getType())).equals(p))
                .count();

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
    public int getInfluence(Team t, ArrayList<Student> students, int size, Faction tower, HashMap<Colour, Player>professors) {
        int influence = 0;
        if(t.getFaction() == tower) {
            influence += getInfluence(t.getLeader(), students, size, tower,professors) + getInfluence(t.getMember(), students, size, tower,professors);
        }
        return influence;
    }
}
