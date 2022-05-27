package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;

public class MoveMotherNatureMessage implements Message{

    private final int playerId;
    private final int moves;

    public MoveMotherNatureMessage(int playerId, int moves) {
        this.playerId = playerId;
        this.moves = moves;
    }

    public int getPlayerID() {
        return playerId;
    }

    public int getMoves() {
        return moves;
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
