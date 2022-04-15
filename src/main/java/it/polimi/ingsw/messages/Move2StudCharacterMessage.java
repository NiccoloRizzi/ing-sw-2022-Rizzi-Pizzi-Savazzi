package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class Move2StudCharacterMessage implements Message{

    private final int charId;
    private final int playerId;
    private final Colour[] stud;
    private final Colour[] stud_2;

    public Move2StudCharacterMessage(int charId, int playerId, Colour[] stud, Colour[] stud_2) {
        this.charId = charId;
        this.playerId = playerId;
        this.stud = stud.clone();
        this.stud_2 = stud_2;
    }

    public Colour[] getStud_2() {
        return stud_2;
    }

    public int getCharId() {
        return charId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public Colour[] getStud() {
        return stud;
    }

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }
}
