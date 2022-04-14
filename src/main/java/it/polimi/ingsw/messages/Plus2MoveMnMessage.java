package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class Plus2MoveMnMessage implements Message{

    private final int characterId;
    private final int playerId;

    public Plus2MoveMnMessage(int characterId, int playerId) {
        this.characterId = characterId;
        this.playerId = playerId;
    }

    public int getCharacterId() {
        return characterId;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visitPlus2MoveMnMessage(this);
    }
}
