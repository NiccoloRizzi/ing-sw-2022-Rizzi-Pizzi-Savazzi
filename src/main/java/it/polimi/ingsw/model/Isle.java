package it.polimi.ingsw.model;

/*UML Changes
 * 1) Removed size and influence
 * 2) Attributed changed to protected
 * 3) Added setFaction()
 * 4) Added getFaction()*/

public class Isle extends Tile {

    protected Faction torre;
    protected boolean prohibited;

    /**
     * Default constructor
     */
    public Isle() {
        super();
        torre = Faction.Empty;
    }

    public void setTorre(Faction f) {
        this.torre = f;
    }

    public int getInfluence(Player p) {
        int influence = 0;
        boolean[] temp = p.getBoard().getProfessors();
        for(Student s: students)
            if(temp[s.getType().ordinal()])
                influence++;
        if(p.getBoard().getFaction() == torre) // CORRECT???
            influence ++;
        return influence;
    }

    public int getInfluence(Team t){
        int influence = 0;
        influence += getInfluence(t.getLeader())+ getInfluence(t.getMember())-1;
        return 0;
    }

    public void setProhibited(){ prohibited = true;}

    public boolean removeProhibited()
    {
        boolean temp = prohibited;
        prohibited = false;
        return temp;
    }

    public Faction getFaction(){
        return this.torre;
    }

    public void setFaction(Faction faction){
        this.torre = faction;
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