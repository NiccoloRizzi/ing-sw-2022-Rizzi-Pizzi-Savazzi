package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.ModelSerializer;

/**
 * Message sent to the players to convey Win (or draw) events
 */
public class WinMessage implements ClientModel {
    /**
     * The id of the winning player, if existing
     */
    private int id;
    /**
     * Boolean to signal if it's a draw
     */
    private final boolean draw;

    /**
     * WinMessage constructor in case there's a winner
     * @param id The id of the winning player
     */
    public WinMessage(int id){
        this.id = id;
        this.draw = false;
    }
    /**
     * WinMessage constructor in case it's a draw
     * @param draw Boolean signaling that it's a draw
     */
    public WinMessage(boolean draw){
        this.draw = draw;
    }

    /**
     * Getter for Player's ID
     * @return Player's ID
     */
    public int getId() {
        return id;
    }
    /**
     * Accept method for visitor pattern
     * @param visitor The client-side visitor that handles server updates
     */
    public void accept(View visitor)
    {
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

    /**
     * Returns whether the match resulted in a draw
     * @return whether the match resulted in a draw
     */
    public boolean isDraw() {
        return draw;
    }
}
