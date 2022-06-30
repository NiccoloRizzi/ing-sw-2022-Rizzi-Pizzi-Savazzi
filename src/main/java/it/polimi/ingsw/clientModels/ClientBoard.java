package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.HashMap;

/**
 * Client-side version of the character class containing information of interest to the players and that can be sent as an update
 */
public class ClientBoard implements ClientModel{
    /**
     * The ID of the board's owner
     */
    private final int playerID;
    /**
     * The faction of the board
     */
    private final Faction faction;
    /**
     * The number of towers on the board
     */
    private final int towers;
    /**
     * Students placed at tables
     */
    private final HashMap<Colour, Integer> tables;
    /**
     * Movable students in the entrance
     */
    private final HashMap<Colour, Integer> entrance;

    /**
     * Constructor for ClientBoard
     * @param playerID The ID of the board's owner
     * @param faction Faction of the board
     * @param towers Number of towers on the board
     * @param tables Students placed in the tables
     * @param entrance Movable students in the entrance
     */
    public ClientBoard(int playerID,Faction faction,int towers,HashMap<Colour,Integer> tables,HashMap<Colour,Integer> entrance)
    {
        this.playerID = playerID;
        this.faction = faction;
        this.towers = towers;
        this.tables = tables;
        this.entrance = entrance;
    }

    /**
     * Getter for player's ID
     * @return Player's ID
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Getter for board's faction
     * @return Board's faction
     */
    public Faction getFaction() {
        return faction;
    }

    /**
     * Getter for number of towers
     * @return Number of towers
     */
    public int getTowers() {
        return towers;
    }

    /**
     * Getter for tables of students
     * @return HashMap containing the students placed at tables
     */
    public HashMap<Colour, Integer> getTables() {
        return tables;
    }

    /**
     * Getter for students at the entrance
     * @return HashMap containing movable  students at the entrance
     */
    public HashMap<Colour, Integer> getEntrance() {
        return entrance;
    }

    /**
     * Constructor for ClientBoard using another ClientBoard
     * @param board ClientBoard to clone
     */
    public ClientBoard(ClientBoard board){
        this.playerID = board.playerID;
        this.entrance = new HashMap<>(board.entrance);
        this.faction = board.faction;
        this.towers = board.towers;
        this.tables = new HashMap<>(board.tables);
    }
    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }
    /**
     * Accept method for visitor pattern
     * @param view Client-view, acting as a Visitor for server updates
     */
    @Override
    public void accept(View view) {
        view.visit(this);
    }
}
