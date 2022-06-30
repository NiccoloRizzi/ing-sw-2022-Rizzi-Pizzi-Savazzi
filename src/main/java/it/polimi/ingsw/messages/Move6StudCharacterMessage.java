package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

/**
 * Message for using the character that allows the player to exchange students between tables and entrance
 */
public class Move6StudCharacterMessage implements Message{
    /**
     * The id of the character being used
     */
    private final int charId;
    /**
     * The id of the player being used
     */
    private final int playerId;
    /**
     * Students to be taken from the tables
     */
    private final Colour[] studs;
    /**
     * Students to be taken from the board
     */
    private final Colour[] studs_2;

    /**
     * Creates a message for taking students from the character
     * @param charId The id of the character being used
     * @param playerId The id of the player being used
     * @param studFromBoard Students to be taken from the tables
     * @param studFromChar Students to be taken from the board
     */
    public Move6StudCharacterMessage(int charId, int playerId, Colour[] studFromBoard, Colour[] studFromChar) {
        this.charId = charId;
        this.playerId = playerId;

        this.studs = studFromBoard.clone();
        this.studs_2 = studFromChar.clone();
    }

    /**
     *
     * @return Used character's id
     */
    public int getCharacterID() {
        return charId;
    }

    /**
     *
     * @return Students taken from the character
     */
    public Colour[] getStudsFromChar() {
        return studs_2;
    }

    /**
     *
     * @return The player's id
     */
    public int getPlayerID() {
        return playerId;
    }

    public Colour[] getStudFromBoard() {
        return studs;
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
    @Override
    public String serialize(){
        return MessageSerializer.serialize(this);
    }
}
