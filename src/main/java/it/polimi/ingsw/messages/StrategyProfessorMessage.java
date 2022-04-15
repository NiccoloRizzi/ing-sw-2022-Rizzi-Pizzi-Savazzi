package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class StrategyProfessorMessage implements Message{

    private final int characterId;
    private final int playerId;

    public StrategyProfessorMessage(int characterId, int playerId) {
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
        visitor.visit(this);
    }
}