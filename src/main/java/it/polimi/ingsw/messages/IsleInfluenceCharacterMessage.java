package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class IsleInfluenceCharacterMessage implements Message{

    private final int characterId;
    private final int playerId;
    private final int isleIndex;
    private Colour noColour;

    public IsleInfluenceCharacterMessage(int characterId, int playerId, int isleIndex, Colour noColour) {
        this.characterId = characterId;
        this.playerId = playerId;
        this.isleIndex = isleIndex;
        this.noColour = noColour;
    }

    public IsleInfluenceCharacterMessage(int characterId, int playerId, int isleIndex) {
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

    public Colour getNoColour() {
        return noColour;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
