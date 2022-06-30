package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

/**
 * Message for choosing an assistant
 */
public class AssistantChoiceMessage implements Message{

    /**
     * The chosen assistant's id
     */
    private final int assistantId;
    /**
     * The id of the player sending the message
     */
    private final int playerId;


    /**
     * Creates the message with the given assistant and player
     * @param assistantId The assistant's id
     * @param playerId The player's id
     */
    public AssistantChoiceMessage(int assistantId, int playerId) {
        this.assistantId = assistantId;
        this.playerId = playerId;
    }

    /**
     * Getter for the assistant's ID
     * @return The assistant's ID
     */
    public int getAssistantID() {
        return assistantId;
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
