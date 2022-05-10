package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientIsle;

import java.util.HashMap;

public class Isle extends Tile {

    private Faction tower;
    private int prohibited;
    private influenceStrategy infStrategy;
    private int size;


    /**
     * Default constructor
     */
    public Isle(int Id) {
        super(Id);
        tower = Faction.Empty;
        infStrategy= new DefaultInfStrategy();
        size = 1;
    }

    public void setTower(Faction f) {
        this.tower = f;
    }

    public int getSize(){
        return size;
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

    public Isle join (Isle isle)
    {
        for(Colour c: Colour.values()){
            this.students.put(c,this.students.get(c)+isle.students.get(c));
        }
        this.size += isle.size;
        return this;
    }


    public void setInfStrategy (influenceStrategy infStrategy)
    {
        this.infStrategy=infStrategy;
    }

    public void notifyChange()
    {
        notify(new ClientIsle(ID,tower,new HashMap<Colour, Integer>(students)));
    }
}