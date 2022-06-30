package it.polimi.ingsw.messages;

/**
 * Message sent by the player to the server to send his information and the type of match he wants to play in
 */
public class PlayerMessage {
    /**
     * Nickname chosen by the player
     */
    private final String nickname;
    /**
     * Number of players chosen by the player
     */
    private final int playersNumber;
    /**
     * Whether the player wants to play in expert mode
     */
    private final boolean expertMode;

    /**
     * Constructor for PlayerMessage
     * @param nickname Nickname chosen by the player
     * @param playersNumber Number of players chosen by the player
     * @param expertMode Whether the player wants to play in expert mode
     */
    public PlayerMessage(String nickname, int playersNumber, boolean expertMode){
        this.nickname = nickname;
        this.playersNumber = playersNumber;
        this.expertMode = expertMode;
    }

    /**
     * Getter for player's Nickname
     * @return Player's Nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for the number of players chosen by the player
     * @return The number of players chosen by the player
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * Whether the player wants to play in expert mode
     * @return Whether the player wants to play in expert mode
     */
    public boolean isExpertMode() {
        return expertMode;
    }

}
