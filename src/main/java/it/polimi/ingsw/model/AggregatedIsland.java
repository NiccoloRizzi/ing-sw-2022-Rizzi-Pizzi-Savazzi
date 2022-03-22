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
        this.setTower(i1.getTower());
    }

    public int getSize() {return size;}

    @Override
    public int getInfluence(Player p) {
            return infStrategy.getInfluence(p,students,size,tower);
    }
    @Override
    public int getInfluence(Team t){
        return infStrategy.getInfluence(t,students,size,tower);
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