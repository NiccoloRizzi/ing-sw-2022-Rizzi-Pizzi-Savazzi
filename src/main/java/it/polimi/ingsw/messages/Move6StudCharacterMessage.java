package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class Move6StudCharacterMessage implements Message{
    private final int charId;
    private final int playerId;
    private final Colour[] studs; //SINGLE???
    private final Colour[] studs_2; // SINGLE???

    public Move6StudCharacterMessage(int charId, int playerId, Colour[] studFromBoard, Colour[] studFromChar) {
        this.charId = charId;
        this.playerId = playerId;

        this.studs = studFromBoard.clone();
        this.studs_2 = studFromChar.clone();
    }

    public int getCharacterID() {
        return charId;
    }

    public Colour[] getStudsFromChar() {
        return studs_2;
    }

    public int getPlayerID() {
        return playerId;
    }

    public Colour[] getStudFromBoard() {
        return studs;
    }

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }
}
