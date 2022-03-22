package it.polimi.ingsw.model;

import java.util.ArrayList;

public class PlusInfStrategy {

    public int getInfluence(Player p, ArrayList<Student> students, int size, Faction tower) {
        int influence = 0;
        if (tower.equals(p.getBoard().getFaction())) {
            influence += size;
        }
        influence += students.stream()
                .filter(student -> p.getBoard().getProfessors()[student.getType().ordinal()])
                .count();

        return influence + 2;
    }

    public int getInfluence(Team t, ArrayList<Student> students, int size, Faction tower){
        return getInfluence(t.getLeader(), students, size, tower)+ getInfluence(t.getMember(), students, size, tower)-2-size;
    }
}
