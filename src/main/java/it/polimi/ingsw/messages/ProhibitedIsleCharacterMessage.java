package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

/**
 * Message for using the character that prohibits an island
 */
public class ProhibitedIsleCharacterMessage implements Message{

    /**
     * The id of the character being used
     */
    private final int charId;
    /**
     * The id of the player using the character
     */
    private final int playerId;
    /**
     * The index of the isle the prohibition will be applied to
     */
    private final int isleIndex;

    /**
     * Creates a message for using the character that prohibits an island
     * @param charId The id of the character being used
     * @param playerId The id of the player using the character
     * @param isleIndex The index of the isle the prohibition will be applied to
     */

    public ProhibitedIsleCharacterMessage(int charId, int playerId, int isleIndex) {
        this.charId = charId;
        this.playerId = playerId;
        this.isleIndex = isleIndex;
    }

    /**
     *
     * @return Used character's id
     */
    public int getCharacterID() {
        return charId;
    }

    /**
     *
     * @return The player's id
     */
    public int getPlayerID() {
        return playerId;
    }

    /**
     *
     * @return the index of the isle the prohibition will be applied to
     */
    public int getIsleID() {
        return isleIndex;
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
