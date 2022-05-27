package it.polimi.ingsw.messages;

public class WinDisconnection {
    private final int playerId;

    public WinDisconnection(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}