package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class MoveStudentMessage implements Message{

    private final int playerId;
    private final Colour student;
    private final int tileIndex;
    private final boolean toTable;

    public MoveStudentMessage(int playerId, Colour student, int tileIndex, boolean toTable) {
        this.playerId = playerId;
        this.student = student;
        this.tileIndex = tileIndex;
        this.toTable = toTable;
    }

    public int getPlayerID() {
        return playerId;
    }

    public Colour getStudent() {
        return student;
    }

    public int getTileID() {
        return tileIndex;
    }

    public boolean isToTable() {
        return toTable;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }
}
