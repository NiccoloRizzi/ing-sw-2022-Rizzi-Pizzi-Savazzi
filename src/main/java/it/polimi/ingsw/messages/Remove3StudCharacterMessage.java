package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class Remove3StudCharacterMessage implements Message{
    private final int charId;
    private final int playerId;
    private final Colour colour;

    public Remove3StudCharacterMessage(int charId, int playerId, Colour colour) {
        this.charId = charId;
        this.playerId = playerId;
        this.colour = colour;
    }

    public int getCharacterID() {
        return charId;
    }

    public int getPlayerID() {
        return playerId;
    }

    public Colour getColour() {
        return colour;
    }

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }
}
