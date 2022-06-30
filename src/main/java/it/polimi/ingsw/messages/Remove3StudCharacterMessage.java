package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

/**
 * Message for using the character that removes 3 students of a given colour from players' tables
 */
public class Remove3StudCharacterMessage implements Message{
    /**
     * The id of the character being used
     */
    private final int charId;
    /**
     * The id of the player using the character
     */
    private final int playerId;
    /**
     * The colour of the students that will be removed
     */
    private final Colour colour;

    /**
     * Creates a message for using the character that removes 3 students of a given colour from players' tables
     * @param charId The id of the character being used
     * @param playerId The id of the player using the character
     * @param colour The colour of the students that will be removed
     */
    public Remove3StudCharacterMessage(int charId, int playerId, Colour colour) {
        this.charId = charId;
        this.playerId = playerId;
        this.colour = colour;
    }

    /**
     * Getter for used character's ID
     * @return Used character's ID
     */
    public int getCharacterID() {
        return charId;
    }

    /**
     * Getter for the player's ID
     * @return The player's ID
     */
    public int getPlayerID() {
        return playerId;
    }

    /**
     * Getter for the colour of the students that will be removed
     * @return The colour of the students that will be removed
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Accept method for visitor pattern
     * @param visitor The MessageVisitor that handles players' moves
     */
    @Override
    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }

    /**
     * Method for serializing the message in Json format
     * @return Serialized message
     */
    @Override
    public String serialize(){
        return MessageSerializer.serialize(this);
    }
}
