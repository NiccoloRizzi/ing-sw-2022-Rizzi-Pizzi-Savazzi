package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

/**
 * Message for moving mother nature
 */
public class MoveMotherNatureMessage implements Message{

    /**
     * Id of the player
     */
    private final int playerId;
    /**
     * The amount isles mother nature will be moved for
     */
    private final int moves;

    /**
     * Creates a message for moving mother nature
     * @param playerId The id of the player performing the action
     * @param moves The amount isles mother nature will be moved for
     */
    public MoveMotherNatureMessage(int playerId, int moves) {
        this.playerId = playerId;
        this.moves = moves;
    }

    /**
     *
     * @return The player's id
     */
    public int getPlayerID() {
        return playerId;
    }

    public int getMoves() {
        return moves;
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
