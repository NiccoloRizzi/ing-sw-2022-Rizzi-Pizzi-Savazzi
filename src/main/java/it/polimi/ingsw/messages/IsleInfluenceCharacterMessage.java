package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.model.Colour;

public class IsleInfluenceCharacterMessage implements Message{

    private final int characterId;
    private final int playerId;
    private Colour noColour;

    public IsleInfluenceCharacterMessage(int characterId, int playerId,  Colour noColour) {
        this.characterId = characterId;
        this.playerId = playerId;
        this.noColour = noColour;
    }

    public IsleInfluenceCharacterMessage(int characterId, int playerId) {
        this.characterId = characterId;
        this.playerId = playerId;
    }

    public int getCharacterID() {
        return characterId;
    }

    public int getPlayerID() {
        return playerId;
    }


    public Colour getNoColour() {
        return noColour;
    }

    @Override
    public void accept(MessageVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String serialize(){
        return MessageSerializer.serialize(this);
    }
}
