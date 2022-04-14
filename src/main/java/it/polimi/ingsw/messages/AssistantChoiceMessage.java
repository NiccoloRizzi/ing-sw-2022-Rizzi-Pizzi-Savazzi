package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class AssistantChoiceMessage implements Message{

    private final int assistantId;
    private final int playerId;

    public AssistantChoiceMessage(int assistantId, int playerId) {
        this.assistantId = assistantId;
        this.playerId = playerId;
    }

    public int getAssistantId() {
        return assistantId;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visitAssistantChoiceMessage(this);
    }
}
