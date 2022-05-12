package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;

public class TurnMessage implements ClientModel {
    private final int playerId;
    private final Turn turn;

    public TurnMessage (int id, Turn turn){
        this.playerId = id;
        this.turn = turn;
    }

    public enum Turn{
        PLANNING,
        ACTION_STUDENTS,
        ACTION_MN,
        ACTION_CLOUDS
    }

    public void accept(View visitor)
    {
        visitor.visit(this);
    }
}
