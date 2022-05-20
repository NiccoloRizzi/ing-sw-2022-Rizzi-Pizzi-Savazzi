package it.polimi.ingsw.messages;

public class PlayerMessage {
    private final String nickname;
    private final int playersNumber;
    private final boolean expertMode;

    public PlayerMessage(String nickname, int playersNumber, boolean expertMode){
        this.nickname = nickname;
        this.playersNumber = playersNumber;
        this.expertMode = expertMode;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

}
