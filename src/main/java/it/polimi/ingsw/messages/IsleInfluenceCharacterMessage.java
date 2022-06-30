package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

/**
 * Message for using characters that change the strategy used to calculate the influence over an island
 */
public class IsleInfluenceCharacterMessage implements Message{

    /**
     * Used character's id
     */
    private final int characterId;
    /**
     * The id of the player using the character
     */
    private final int playerId;
    /**
     * Ignored colour in NoColourStrategy
     */
    private Colour noColour;

    /**
     * Generates a message to set up the influence strategy that ignores a colour
     * @param characterId The id (in this match) of the used character
     * @param playerId The player using the character
     * @param noColour The colour to be ignored
     */
    public IsleInfluenceCharacterMessage(int characterId, int playerId,  Colour noColour) {
        this.characterId = characterId;
        this.playerId = playerId;
        this.noColour = noColour;
    }

    /**
     * Generates a message to use the character to change influence strategy
     * @param characterId The id (in this match) of the used character
     * @param playerId The player using the character
     */
    public IsleInfluenceCharacterMessage(int characterId, int playerId) {
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
     * Getter for the colour to ignore in the NoColour strategy
     * @return The colour to ignore in the NoColour strategy
     */
    public Colour getNoColour() {
        return noColour;
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
