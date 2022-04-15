package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class MoveStudentMessage implements Message{

    private final int playerId;
    private final Colour student;
    private final int tileIndex;
    private final int type;

    public MoveStudentMessage(int playerId, Colour student, int tileIndex, int type) {
        this.playerId = playerId;
        this.student = student;
        this.tileIndex = tileIndex;
        this.type = type;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Colour getStudentIndex() {
        return student;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public int getType() {
        return type;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
