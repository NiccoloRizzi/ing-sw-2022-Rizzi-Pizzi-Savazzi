package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

public class SimilMotherNatureMesage implements Message{

    private final int characterId;
    private final int playerId;
    private final int isleIndex;

    public SimilMotherNatureMesage(int characterId, int playerId, int isleIndex) {
        this.characterId = characterId;
        this.playerId = playerId;
        this.isleIndex = isleIndex;
    }

    public int getCharacterID() {
        return characterId;
    }

    public int getPlayerID() {
        return playerId;
    }

    public int getIsleID() {
        return isleIndex;
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
