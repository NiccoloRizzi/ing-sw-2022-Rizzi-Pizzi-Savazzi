package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.HashMap;

/**
 * Client-side version of Cloud class containing information of interest to the players and that can be sent as an update
 */
public class ClientCloud implements ClientModel{
    /**
     * Id of the cloud
     */
    private final int id;
    /**
     * Students placed on the cloud
     */
    private final HashMap<Colour, Integer> students;

    /**
     * Constructor for ClientCloud
     * @param id ID of the cloud
     * @param students Students placed on the cloud
     */
    public ClientCloud (int id, HashMap<Colour, Integer> students)
    {
        this.id = id;
        this.students = students;
    }

    /**
     * Getter for Cloud's ID
     * @return Cloud's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for students placed on the cloud
     * @return Students placed on the cloud
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
