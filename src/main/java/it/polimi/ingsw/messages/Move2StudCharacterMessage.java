package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

/**
 * Message for using the character that exchanges students between tables and boards
 */
public class Move2StudCharacterMessage implements Message{

    /**
     * The character's id
     */
    private final int charId;
    /**
     * The player's id
     */
    private final int playerId;
    /**
     * The students removed from the board
     */
    private final Colour[] stud;
    /**
     * The students removed from the tables
     */
    private final Colour[] stud_2;

    /**
     * Creates a message for using the character that exchanges students between tables and board
     * @param charId Used character's id
     * @param playerId Player's id
     * @param studFromBoard Students that will be taken from the board and added to the tables
     * @param studFromTables Students that will be taken from the tables and added to the board
     */
    public Move2StudCharacterMessage(int charId, int playerId, Colour[] studFromBoard, Colour[] studFromTables) {
        this.charId = charId;
        this.playerId = playerId;
        this.stud = studFromBoard.clone();
        this.stud_2 = studFromTables;
    }

    /**
     * Getter for the students that will be removed from the tables and added to the board
     * @return The students that will be removed from the tables and added to the board
     */
    public Colour[] getStudFromTables() {
        return stud_2;
    }

    /**
     * Getter for used character's id
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
     * @return The students that will be removed from the board and added to the table
     */
    public Colour[] getStudFromBoard() {
        return stud;
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
    public String serialize(){
        return MessageSerializer.serialize(this);
    }
}
