package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.MoveSerializer;

public class MoveStudentCharacterMessage implements Message{

    private final int playerId;
    private final int characterId;
    private final Colour student;
    private final int tileIndex;
    // private final int type;

    public MoveStudentCharacterMessage(int playerId, int characterId, Colour student, int tileIndex) {
        this.playerId = playerId;
        this.characterId = characterId;
        this.student = student;
        this.tileIndex = tileIndex;
        // this.type = type; REDUNDANT
    }

    public int getPlayerID() {
        return playerId;
    }

    public int getCharacterID() {
        return characterId;
    }

    public Colour getStudent() {
        return student;
    }

    public int getTileID() {
        return tileIndex;
    }

//    public int getType() {
//        return type;
//    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String serialize(){
        return MoveSerializer.serialize(this);
    }
}
