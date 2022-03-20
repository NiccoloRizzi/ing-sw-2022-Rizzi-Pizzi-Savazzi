package it.polimi.ingsw.model;


public class Isle extends Tile {

    private Faction torre;
    private  boolean prohibited;

    /**
     * Default constructor
     */
    public Isle() {
        super();
        torre = Faction.Empty;
    }

    /**
     * @param Faction
     * @return
     */
    public void setTorre(Faction f) {
        this.torre = f;
    }

    /**
     * @param Player
     * @return
     */
    public int getInfluence(Player p) {
        int influence = 0;
        boolean[] temp = p.getBoard().getProfessors();
        for(Student s: students)
            if(temp[s.getType.ordinal()])
                influence++;
        if(p.getBoard().getFaction())
            influence ++;
        return influence;
    }

    /**
     * @param Team
     * @return
     */
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

    public AggregatedIsland join (Isle isle)
    {
        return new AggregatedIsland(this ,isle);
    }
    @overload
    public AggregatedIsland join (AggregatedIsle isle)
    {
        return isle.Join(this);
    }
}