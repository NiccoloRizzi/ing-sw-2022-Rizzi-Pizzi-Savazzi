package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

/**
 * Message for using the character that increases maximum mother nature moves
 */
public class Plus2MoveMnMessage implements Message{

    /**
     * The id of the character being used
     */
    private final int characterId;
    /**
     * The id of the player that uses the character
     */
    private final int playerId;

    /**
     * Creates a message for using the character that increases maximum mother nature moves
     * @param characterId Used character's id
     * @param playerId The id of the player that uses the character
     */
    public Plus2MoveMnMessage(int characterId, int playerId) {
        this.characterId = characterId;
        this.playerId = playerId;
    }

    /**
     * Getter for used character's ID
     * @return Used character's ID
     */
    public int getCharacterID() {
        return characterId;
    }

    /**
     * Getter for the player's ID
     * @return The player's ID
     */
    public int getPlayerID() {
        return playerId;
    }

    /**
     * Accept method for visitor pattern
     * @param visitor The MessageVisitor that handles players' moves
     */
    @Override
    public void accept(MessageVisitor visitor) {
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
