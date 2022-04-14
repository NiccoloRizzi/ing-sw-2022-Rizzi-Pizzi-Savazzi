package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class Move2StudCharacterMessage implements Message{

    private final int charId;
    private final int playerId;
    private final int[] stud; // SINGLE???

    public Move2StudCharacterMessage(int charId, int playerId, int[] stud) {
        this.charId = charId;
        this.playerId = playerId;
        this.stud = stud.clone();
    }

    public int getCharId() {
        return charId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int[] getStud() {
        return stud;
    }

    public void accept(MessageVisitor visitor){
        visitor.visitMove2StudCharacterMessage(this);
    }
}
