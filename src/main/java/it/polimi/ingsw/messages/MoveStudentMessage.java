package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class MoveStudentMessage implements Message{

    private final int playerId;
    private final int studentIndex;
    private final int tileIndex;
    private final int type;

    public MoveStudentMessage(int playerId, int studentIndex, int tileIndex, int type) {
        this.playerId = playerId;
        this.studentIndex = studentIndex;
        this.tileIndex = tileIndex;
        this.type = type;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getStudentIndex() {
        return studentIndex;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public int getType() {
        return type;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visitMoveStudentMessage(this);
    }
}
