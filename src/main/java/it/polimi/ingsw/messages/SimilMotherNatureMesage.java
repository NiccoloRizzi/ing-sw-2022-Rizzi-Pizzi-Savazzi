package it.polimi.ingsw.messages;

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

    public int getCharacterId() {
        return characterId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getIsleIndex() {
        return isleIndex;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
