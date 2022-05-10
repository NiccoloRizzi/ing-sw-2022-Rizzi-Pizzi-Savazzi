package it.polimi.ingsw.messages.Answers;

public class TurnMessage {
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
}
