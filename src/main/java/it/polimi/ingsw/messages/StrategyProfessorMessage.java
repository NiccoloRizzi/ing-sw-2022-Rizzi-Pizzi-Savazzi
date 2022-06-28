package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

/**
 * Message for using the character that changes the strategy used to determine who owns a professor
 */
public class StrategyProfessorMessage implements Message{

    /**
     * Id of the character being used
     */
    private final int characterId;
    /**
     * Id of the player using the character
     */
    private final int playerId;

    /**
     * Creates a message for using the character that changes the strategy used to determine who owns a professor
     * @param characterId Id of the character being used
     * @param playerId Id of the player using the character
     */
    public StrategyProfessorMessage(int characterId, int playerId) {
        this.characterId = characterId;
        this.playerId = playerId;
    }

    /**
     *
     * @return Used character's id
     */
    public int getCharacterID() {
        return characterId;
    }

    /**
     *
     * @return The player's id
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
