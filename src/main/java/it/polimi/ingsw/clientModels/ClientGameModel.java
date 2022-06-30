package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Client-side version of GameModel class containing information of interest to the players and that can be sent as an update
 */
public class ClientGameModel implements ClientModel{
    /**
     * HashMap linking colours to the player owning the professor of that colour
     */
    private final HashMap<Colour, Integer> professors;
    /**
     * ID of the island motherNature is currently placed on
     */
    private final int motherNature;
    /**
     * List of Isles (as ClientIsles)
     */
    private final ArrayList<ClientIsle> isles;
    /**
     * The amount of available prohibitions usable on islands
     */
    private final int prohibited;

    /**
     * ClientGameModel constructor
     * @param professors HashMap linking colours to the player owning the professor of that colour
     * @param motherNature ID of the island motherNature is currently placed on
     * @param isles List of ClientIsles
     * @param prohibited The amount of available prohibitions usable on islands
     */
    public ClientGameModel(HashMap<Colour, Integer> professors, int motherNature, ArrayList<ClientIsle> isles, int prohibited) {
        this.professors = professors;
        this.motherNature = motherNature;
        this.isles = isles;
        this.prohibited = prohibited;
    }

    /**
     * Getter for the list of isles (as ClientIsles)
     * @return The list of Isles (as clientIsles)
     */
    public ArrayList<ClientIsle> getIsles() {
        return isles;
    }

    /**
     * Getter for professors HashMap
     * @return Professors HashMap
     */
    public HashMap<Colour, Integer> getProfessors() {
        return professors;
    }

    /**
     * Getter for MotherNature current position
     * @return ID of the island mother nature is currently placed at
     */
    public int getMotherNature() {
        return motherNature;
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
     * Getter for the number of available prohibitions for isles
     * @return The number of available prohibitions for isles
     */
    public int getProhibited() {
        return prohibited;
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
