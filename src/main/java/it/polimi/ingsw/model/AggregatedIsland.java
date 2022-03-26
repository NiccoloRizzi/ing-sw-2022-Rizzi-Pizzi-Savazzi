package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

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
        this.setTower(i1.getTower());
    }

    public int getSize() {return size;}

    @Override
    public int getInfluence(Player p, HashMap<Colour,Player> professors) {
            return infStrategy.getInfluence(p,students,size,tower,professors);
    }
    @Override
    public int getInfluence(Team t,HashMap<Colour,Player> professors){
        return infStrategy.getInfluence(t,students,size,tower,professors);
    }
    @Override
    public int getInfluenceNoColour(Player p, Colour c)
    {
        int influence = 0;
        influence+= students.stream()
                .filter(student -> p.getBoard().getProfessors()[student.getType().ordinal()]&& c!=(student.getType()))
                .count();

        if(p.getBoard().getFaction() == tower)
            influence += size;
        return influence;
    }
    @Override
    public int getInfluenceNoColour(Team t, Colour c)
    {
        return getInfluenceNoColour(t.getLeader(),c)+ getInfluenceNoColour(t.getMember(),c)-size;
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