package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class Move6StudCharacterMessage implements Message{
    private final int charId;
    private final int playerId;
    private final Colour[] studs; //SINGLE???
    private final Colour[] studs_2; // SINGLE???

    public Move6StudCharacterMessage(int charId, int playerId, Colour[] studs, Colour[] studs_2) {
        this.charId = charId;
        this.playerId = playerId;

        this.studs = studs.clone();
        this.studs_2 = studs_2.clone();
    }

    public int getCharId() {
        return charId;
    }

    public Colour[] getStuds_2() {
        return studs_2;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Colour[] getStuds() {
        return studs;
    }

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }
}
