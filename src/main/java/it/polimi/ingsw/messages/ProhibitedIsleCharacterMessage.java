package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class ProhibitedIsleCharacterMessage implements Message{

    private final int charId;
    private final int playerId;
    private final int isleIndex;

    public ProhibitedIsleCharacterMessage(int charId, int playerId, int isleIndex) {
        this.charId = charId;
        this.playerId = playerId;
        this.isleIndex = isleIndex;
    }

    public int getCharacterID() {
        return charId;
    }

    public int getPlayerID() {
        return playerId;
    }

    public int getIsleID() {
        return isleIndex;
    }

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }
}
