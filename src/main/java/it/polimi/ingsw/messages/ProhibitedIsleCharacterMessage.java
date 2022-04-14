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

    public int getCharId() {
        return charId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getIsleIndex() {
        return isleIndex;
    }

    public void accept(MessageVisitor visitor){
        visitor.visitProhibitedIsleCharacterMessage(this);
    }
}
