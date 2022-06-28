package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

/**
 * Message for choosing the cloud at turn's end
 */
public class CloudChoiceMessage implements Message{

    /**
     * The id of the player choosing the cloud
     */
    private final int playerId;
    /**
     * Chosen cloud's id
     */
    private final int cloudIndex;

    /**
     * Generates the message with the given player and cloud ids
     * @param playerId Player's id
     * @param cloudIndex Cloud's id
     */
    public CloudChoiceMessage(int playerId, int cloudIndex) {
        this.playerId = playerId;
        this.cloudIndex = cloudIndex;
    }

    /**
     *
     * @return The player's id
     */
    public int getPlayerId() {
        return playerId;
    }

    public int getCloudID() {
        return cloudIndex;
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
