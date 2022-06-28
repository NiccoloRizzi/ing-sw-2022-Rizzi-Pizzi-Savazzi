package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

/**
 * Message for moving a student from a character that can contain them
 */
public class MoveStudentCharacterMessage implements Message{

    /**
     * The id of the player using the character
     */
    private final int playerId;
    /**
     * Used character's id
     */
    private final int characterId;
    /**
     * Colour of the student to be moved
     */
    private final Colour student;
    /**
     * Index of the tile where the student will be moved to
     */
    private final int tileIndex;
    // private final int type;

    /**
     * Creates a message for moving a student from a character that can contain them
     * @param playerId The player using the character
     * @param characterId The id of the used character
     * @param student The colour of the student being moved
     * @param tileIndex The index of the tile where the student will be moved to
     */
    public MoveStudentCharacterMessage(int playerId, int characterId, Colour student, int tileIndex) {
        this.playerId = playerId;
        this.characterId = characterId;
        this.student = student;
        this.tileIndex = tileIndex;
        // this.type = type; REDUNDANT
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
     * @return Used character's id
     */
    public int getCharacterID() {
        return characterId;
    }

    /**
     *
     * @return The colour of the student that will be moved
     */
    public Colour getStudent() {
        return student;
    }

    /**
     *
     * @return The id of the tile where the student will be moved to
     */
    public int getTileID() {
        return tileIndex;
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
