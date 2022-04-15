package it.polimi.ingsw.model;

import java.util.HashMap;

public class Isle extends Tile {

    protected Faction tower;
    protected int prohibited;
    protected influenceStrategy   infStrategy;

    /**
     * Default constructor
     */
    public Isle() {
        super();
        tower = Faction.Empty;
        infStrategy= new DefaultInfStrategy();
    }

    public void setTower(Faction f) {
        this.tower = f;
    }

    public int getInfluence(Player p, HashMap<Colour,Player> professors) {
        return infStrategy.getInfluence(p,students,1,tower, professors);
    }

    public int getInfluence(Team t,HashMap<Colour,Player> professors){
        return infStrategy.getInfluence(t,students,1,tower, professors);
    }

    public void setProhibited(){ prohibited ++;}

    public boolean removeProhibited()
    {
       if(prohibited > 0) {
           prohibited--;
           return true;
       }
        return false;
    }

    public Faction getTower(){
        return this.tower;
    }

    public AggregatedIsland join (Isle isle)
    {
        return new AggregatedIsland(this ,isle);
    }

    public AggregatedIsland join (AggregatedIsland isle)
    {
        return isle.join(this);
    }

    public void setInfStrategy (influenceStrategy infStrategy)
    {
        this.infStrategy=infStrategy;
    }
}