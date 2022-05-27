package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

public class StrategyProfessorMessage implements Message{

    private final int characterId;
    private final int playerId;

    public StrategyProfessorMessage(int characterId, int playerId) {
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
