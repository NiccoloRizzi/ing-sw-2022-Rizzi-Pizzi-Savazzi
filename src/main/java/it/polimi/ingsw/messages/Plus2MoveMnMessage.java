package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

public class Plus2MoveMnMessage implements Message{

    private final int characterId;
    private final int playerId;

    public Plus2MoveMnMessage(int characterId, int playerId) {
        this.characterId = characterId;
        this.playerId = playerId;
    }

    public int getCharacterID() {
        return characterId;
    }

    public int getPlayerID() {
        return playerId;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String serialize(){
        return MessageSerializer.serialize(this);
    }
}
