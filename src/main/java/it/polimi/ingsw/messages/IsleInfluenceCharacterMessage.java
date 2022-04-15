package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class IsleInfluenceCharacterMessage implements Message{

    private final int charatcerId;
    private final int playerId;
    private final int isleIndex;
    private Colour noColour;

    public IsleInfluenceCharacterMessage(int charatcerId, int playerId, int isleIndex, Colour noColour) {
        this.charatcerId = charatcerId;
        this.playerId = playerId;
        this.isleIndex = isleIndex;
        this.noColour = noColour;
    }

    public IsleInfluenceCharacterMessage(int charatcerId, int playerId, int isleIndex) {
        this.charatcerId = charatcerId;
        this.playerId = playerId;
        this.isleIndex = isleIndex;
    }

    public int getCharatcerId() {
        return charatcerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getIsleIndex() {
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
