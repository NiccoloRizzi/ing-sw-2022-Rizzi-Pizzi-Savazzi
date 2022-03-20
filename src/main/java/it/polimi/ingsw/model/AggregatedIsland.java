package it.polimi.ingsw.model;

import java.util.ArrayList;

/*UML Changes
 * 1) Added function overload*/

public class AggregatedIsland extends Isle {
    private int size;
    private ArrayList<Isle> joinedIsle;

    public AggregatedIsland (Isle i1,Isle i2)
    {
        super();
        size = 2;
        joinedIsle = new ArrayList<Isle>();
        joinedIsle.add(i1);
        joinedIsle.add(i2);
        this.addStudents(i1.students);
        this.addStudents(i2.students);
        this.setFaction(i1.getFaction());
    }

    public int getSize() {return size;}

    public int getInfluence(Player p) {
        int influence = 0;
        boolean[] temp = p.getBoard().getProfessors();
        for(Student s: students)
            if(temp[s.getType().ordinal()])
                influence++;
        if(p.getBoard().getFaction() == torre)
            influence += size;
        return influence;
    }
    public int getInfluence(Team t){
        int influence = 0;
        influence += getInfluence(t.getLeader())+ getInfluence(t.getMember())-size;
        return 0;
    }

    public AggregatedIsland join (Isle isle)
    {
        size++;
        joinedIsle.add(isle);
        return this;
    }

    public AggregatedIsland join (AggregatedIsland isle)
    {
        size+=isle.getSize();
        joinedIsle.addAll(isle.joinedIsle);
        return this;
    }
}