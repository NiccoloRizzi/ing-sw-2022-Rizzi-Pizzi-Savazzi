package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

/**
 * Interface for a message that represents a move performed by a player
 */
public interface Message {
    /**
     * Accept method for visitor pattern
     * @param visitor The MessageVisitor that handles players' moves
     */
    void accept(MessageVisitor visitor);

    /**
     * Method for serializing the message in Json format
     * @return Serialized message
     */
    String serialize();
}
