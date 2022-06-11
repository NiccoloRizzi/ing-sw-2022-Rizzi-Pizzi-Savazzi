package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientIsle;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;

import java.util.HashMap;

public class Isle extends Tile {

    private Faction tower;
    private int prohibited;
    private influenceStrategy infStrategy;
    private int size;


    /**
     * Generates an Island with a given id
     */
    public Isle(int Id) {
        super(Id);
        tower = Faction.Empty;
        infStrategy= new DefaultInfStrategy();
        size = 1;
    }


    /**
     * Sets the controlling tower of an Island
     *
     * @param faction The faction who is taking the isle
     */
    public void setTower(Faction faction) {
        this.tower = faction;
        //notifyChange();
    }

    /**
     * Gets the size of the island
     *
     * @return The size of the island
     */
    public int getSize(){
        return size;
    }


    /**
     * Calculates the influence on the island of a particular player
     *
     * @return The influence of that player on the island
     */
    public int getInfluence(Player p, HashMap<Colour,Player> professors) {
        return infStrategy.getInfluence(p,students,1,tower, professors);
    }

    /**
     * Calculates the influence on the island of a particular team
     *
     * @return The influence of that team on the island
     */
    public int getInfluence(Team t,HashMap<Colour,Player> professors){
        return infStrategy.getInfluence(t,students,1,tower, professors);
    }

    /**
     * Adds a prohibited to the island
     *
     *
     */
    public void setProhibited(){
        prohibited ++;
        //notifyChange();
    }

    /**
     * Reduces the number of prohibited on the island
     *
     *
     */
    public boolean removeProhibited()
    {
       if(prohibited > 0) {
           prohibited--;
          // notifyChange();
           return true;
       }
        return false;
    }

    /**
     * Gets the faction controlling the island
     *
     * @return the faction controlling the island
     */
    public Faction getTower(){
        return this.tower;
    }

    /**
     * Joins the island with another one given as input
     *
     * @param isle The isle to be joint with
     */
    public Isle join (Isle isle)
    {
        for(Colour c: Colour.values()){
            this.students.put(c,this.students.get(c)+isle.students.get(c));
        }
        this.size += isle.size;
        return this;
    }

    /**
     * Sets a given strategy to calculate the influence of a player or team
     *
     * @param infStrategy The strategy that is being set
     */
    public void setInfStrategy (influenceStrategy infStrategy)
    {
        this.infStrategy=infStrategy;
    }

    public void notifyChange()
    {
        notify(new ClientIsle(ID,tower,new HashMap<Colour, Integer>(students),prohibited,size));
    }

    public ClientIsle getClientIsle(){
        return new ClientIsle(ID,tower,new HashMap<Colour, Integer>(students),prohibited,size);
    }
}