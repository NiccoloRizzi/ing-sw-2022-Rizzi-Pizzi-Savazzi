package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class Move6StudCharacterMessage implements Message{
    private final int charId;
    private final int playerId;
    private final int[] studs; //SINGLE???
    private final int[] studs_2; // SINGLE???

    public Move6StudCharacterMessage(int charId, int playerId, int[] studs, int[] studs_2) {
        this.charId = charId;
        this.playerId = playerId;
        this.studs = studs.clone();
        this.studs_2 = studs_2.clone();
    }

    public int getCharId() {
        return charId;
    }

    public int[] getStuds_2() {
        return studs_2;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int[] getStuds() {
        return studs;
    }

    public void accept(MessageVisitor visitor){
        visitor.visitMove6StudCharacterMessage(this);
    }
}
