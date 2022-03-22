package it.polimi.ingsw.model;

/*UML Changes
 * 1) Removed size and influence
 * 2) Attributed changed to protected
 * 3) Added setFaction()
 * 4) Added getFaction()*/

public class Isle extends Tile {

    protected Faction tower;
    protected boolean prohibited;
    protected influenceStrategy   infStrategy;

    /**
     * Default constructor
     */
    public Isle() {
        super();
        tower = Faction.Empty;
    }

    public void setTower(Faction f) {
        this.tower = f;
    }

    public int getInfluence(Player p) {
        return infStrategy.getInfluence(p,students,1,tower);
    }

    public int getInfluence(Team t){
        return infStrategy.getInfluence(t,students,1,tower);
    }

    public void setProhibited(){ prohibited = true;}

    public boolean removeProhibited()
    {
        boolean temp = prohibited;
        prohibited = false;
        return temp;
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
}