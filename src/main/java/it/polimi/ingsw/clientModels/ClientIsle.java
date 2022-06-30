package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.HashMap;

/**
 * Client-side version of Isle class containing information of interest to the players and that can be sent as an update
 */
public class ClientIsle implements ClientModel{
    /**
     * ID of the isle
     */
    private final int id;
    /**
     * The faction (colour of towers) who currently controls the isle
     */
    private final Faction controlling;
    /**
     * The students placed on the isle
     */
    private final HashMap<Colour, Integer> students;
    /**
     * The size of the isle
     */
    private final int size;
    /**
     * The amount of times this isle has been prohibited
     */
    private final int prohibited;

    /**
     * Constructor for ClientIsle
     * @param id ID of the isle
     * @param controlling The faction (colour of towers) who currently controls the isle
     * @param students The students placed on the isle
     * @param prohibited The amount of times this isle has been prohibited
     * @param size The size of the isle
     */
    public ClientIsle(int id,Faction controlling,HashMap<Colour, Integer> students,int prohibited, int size) {
        this.id = id;
        this.size = size;
        this.controlling = controlling;
        this.students = students;
        this.prohibited=prohibited;
    }

    /**
     * Getter for the size of the island
     * @return Size of the island
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter for the times the isle has been prohibited
     * @return the times the isle has been prohibited
     */
    public int getProhibited() {
        return prohibited;
    }

    /**
     * Getter for Isle's ID
     * @return Isle's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for the faction which currently controls the isle
     * @return The faction which currently controls the isle
     */
    public Faction getControlling() {
        return controlling;
    }

    /**
     * Getter for the students currently placed on the isle
     * @return The students currently placed on the isle
     */
    public HashMap<Colour, Integer> getStudents() {
        return students;
    }
    /**
     * Accept method for visitor pattern
     * @param visitor Client-view, acting as a Visitor for server updates
     */
    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }
}
