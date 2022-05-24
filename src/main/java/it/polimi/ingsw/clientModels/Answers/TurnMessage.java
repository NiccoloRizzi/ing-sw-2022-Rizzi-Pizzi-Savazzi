package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.ModelSerializer;

public class TurnMessage implements ClientModel {
    private final int playerId;
    private final Turn turn;

    public TurnMessage (int id, Turn turn){
        this.playerId = id;
        this.turn = turn;
    }

    public enum Turn{
        PLANNING("Pianificazione, scelta assistente"),
        ACTION_STUDENTS("Azione, spostamento studenti"),
        ACTION_MN("Azione, spostamento madre natura"),
        ACTION_CLOUDS("Azione, scelta della nuvola");

        private final String turnMsg;

        Turn(String value) {
            this.turnMsg = value;
        }

        public String getTurnMsg() {
            return turnMsg;
        }
    }

    public void accept(View visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }

    public int getPlayerId() {
        return playerId;
    }

    public Turn getTurn() {
        return turn;
    }
}
