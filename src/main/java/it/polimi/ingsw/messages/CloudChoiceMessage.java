package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class CloudChoiceMessage implements Message{

    private final int playerId;
    private final int cloudIndex;

    public CloudChoiceMessage(int playerId, int cloudIndex) {
        this.playerId = playerId;
        this.cloudIndex = cloudIndex;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getCloudID() {
        return cloudIndex;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
