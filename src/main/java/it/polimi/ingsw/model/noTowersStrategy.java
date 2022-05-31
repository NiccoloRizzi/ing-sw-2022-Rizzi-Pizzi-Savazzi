package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class noTowersStrategy implements influenceStrategy{
    @Override
    public int getInfluence(Player p, HashMap<Colour,Integer> students, int size, Faction tower, HashMap<Colour, Player> professors) {
        int influence = 0;
            for(Colour c: professors.keySet())
            {
                influence += (professors.get(c).equals(p))?students.get(c):0;
            }
//        if(p.getBoard().getFaction() == tower){
//            for(Student s : students){
//                if(p.getBoard().getProfessors()[s.getType().ordinal()]){
//                    influence++;
//                }
//            }
//        }
        System.out.println(p.getNickname()+"  "+influence);
       return influence;
    }

    @Override
    public int getInfluence(Team t, HashMap<Colour, Integer> students, int size, Faction tower, HashMap<Colour, Player>professors) {
        int influence = 0;
        if(t.getFaction() == tower) {
            influence += getInfluence(t.getLeader(), students, size, tower,professors) + getInfluence(t.getMember(), students, size, tower,professors);
        }
        return influence;
    }
}
