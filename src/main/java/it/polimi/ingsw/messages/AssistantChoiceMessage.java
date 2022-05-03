package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class AssistantChoiceMessage implements Message{

    private final int assistantId;
    private final int playerId;

    public AssistantChoiceMessage(int assistantId, int playerId) {
        this.assistantId = assistantId;
        this.playerId = playerId;
    }

    public int getAssistantID() {
        return assistantId;
    }

    public int getPlayerID() {
        return playerId;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
