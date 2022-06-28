package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

/**
 * Message for moving a student from the board to an isle or to the tables
 */
public class MoveStudentMessage implements Message{

    private final int playerId;
    private final Colour student;
    private final int tileIndex;
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
     *
     * @return The player's id
     */
    public int getPlayerID() {
        return playerId;
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
     *
     * @return whether the student will be moved to tables
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
