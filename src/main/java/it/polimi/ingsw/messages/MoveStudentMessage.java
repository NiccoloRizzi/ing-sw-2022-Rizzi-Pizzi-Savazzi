package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

/**
 * Message for moving a student from the board to an isle or to the tables
 */
public class MoveStudentMessage implements Message{
    /**
     * The ID of the current player
     */
    private final int playerId;
    /**
     * The colour of the student to be moved
     */
    private final Colour student;
    /**
     * The index of the tile the student will be moved to
     */
    private final int tileIndex;
    /**
     * Whether the student will be moved to a table
     */
    private final boolean toTable;

    /**
     * Create a message for moving a student from the board to an isle or to the tables
     * @param playerId The id of the player performing the move
     * @param student The colour of the student being moved
     * @param tileIndex The id of the tile the student will be moved to
     * @param toTable Boolean reporting if the student will be moved to the table or to an isle
     */
    public MoveStudentMessage(int playerId, Colour student, int tileIndex, boolean toTable) {
        this.playerId = playerId;
        this.student = student;
        this.tileIndex = tileIndex;
        this.toTable = toTable;
    }

    /**
     * Getter for the player's ID
     * @return The player's ID
     */
    public int getPlayerID() {
        return playerId;
    }

    /**
     * Getter for the colour of the student that will be moved
     * @return The colour of the student that will be moved
     */
    public Colour getStudent() {
        return student;
    }

    /**
     * Getter for the id of the tile where the student will be moved to
     * @return The id of the tile where the student will be moved to
     */
    public int getTileID() {
        return tileIndex;
    }

    /**
     * Getter for whether the student will be moved to tables
     * @return Whether the student will be moved to tables
     */
    public boolean isToTable() {
        return toTable;
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
