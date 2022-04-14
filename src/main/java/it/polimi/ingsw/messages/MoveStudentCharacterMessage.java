package it.polimi.ingsw.messages;

import it.polimi.ingsw.controller.MessageVisitor;

public class MoveStudentCharacterMessage implements Message{

    private final int playerId;
    private final int characterId;
    private final int studentIndex;
    private final int tileIndex;
    private final int type;

    public MoveStudentCharacterMessage(int playerId, int characterId, int studentIndex, int tileIndex, int type) {
        this.playerId = playerId;
        this.characterId = characterId;
        this.studentIndex = studentIndex;
        this.tileIndex = tileIndex;
        this.type = type;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getCharacterId() {
        return characterId;
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
        visitor.visitMoveStudentCharacterMessage(this);
    }
}
