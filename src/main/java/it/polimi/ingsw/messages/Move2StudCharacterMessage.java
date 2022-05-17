package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.MoveSerializer;

public class Move2StudCharacterMessage implements Message{

    private final int charId;
    private final int playerId;
    private final Colour[] stud;
    private final Colour[] stud_2;

    public Move2StudCharacterMessage(int charId, int playerId, Colour[] studFromBoard, Colour[] studFromTables) {
        this.charId = charId;
        this.playerId = playerId;
        this.stud = studFromBoard.clone();
        this.stud_2 = studFromTables;
    }

    public Colour[] getStudFromTables() {
        return stud_2;
    }

    public int getCharacterID() {
        return charId;
    }

    public int getPlayerID() {
        return playerId;
    }

    public Colour[] getStudFromBoard() {
        return stud;
    }

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }

    public String serialize(){
        return MoveSerializer.serialize(this);
    }
}
