package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelView {
    /**
     * Array of the players
     */
    private final ClientPlayer[] players;
    /**
     * GameModel contaaining all the information needed by the view
     */
    private ClientGameModel gameModel;
    /**
     * Array of the board of the players
     */
    private final ClientBoard[] boards;
    /**
     * Array of the clouds
     */
    private final ClientCloud[] clouds;
    /**
     * Array of the characters
     */
    private ClientCharacter[] characters;
    /**
     * Optional containing a character if it was chosen
     */
    private Optional<Integer> currentCharacter;
    /**
     * Map containig the assistant chosen by other players
     */
    private Map<ClientPlayer, Integer> otherPlayerAssistant;
    /**
     * ErrorType signaling the type of the error if it was raised, otherwise null
     */
    private ErrorMessage.ErrorType error;
    /**
     * Turnmessage with all the information relative to the current turn
     */
    private TurnMessage turn;
    /**
     * WinMessag containg the winner or declaring a tie if the game ended
     */
    private WinMessage win;
    /**
     * String with the nickname of the player
     */
    private final String nickname;
    /**
     * Id of the player assigned by the server
     */
    private int myId;
    /**
     * Boolean for the expert mode
     */
    private final boolean isExpert;

    /**
     * Constructor for the ModelView
     * @param nickname nickname of the player
     * @param nplayers number of players
     * @param expertMode whether the game is in expert mode
     */
    public ModelView(String nickname, int nplayers, boolean expertMode){
        this.nickname = nickname;
        this.isExpert = expertMode;
        if(expertMode){
            characters = new ClientCharacter[3];
        }
        players = new ClientPlayer[nplayers];
        boards = new ClientBoard[nplayers];
        clouds = new ClientCloud[nplayers];
        currentCharacter = Optional.empty();
        otherPlayerAssistant = new HashMap<>();
    }

    /**
     * Getter for error
     * @return error
     */
    public ErrorMessage.ErrorType getError() {
        return error;
    }

    /**
     * Getter for turn
     * @return turn
     */
    public TurnMessage getTurn() {
        return turn;
    }

    /**
     * Getter for currentCharacter
     * @return currentCharacter if it exists
     */
    public Optional<Integer> getCurrentCharacter() {
        return currentCharacter;
    }

    /**
     * Getter for expert
     * @return expert
     */
    public boolean isExpert() {
        return isExpert;
    }

    /**
     * Getter for gameModel
     * @return gameModel
     */
    public synchronized ClientGameModel getGameModel() {
        return gameModel;
    }

    /**
     * Getter for boards
     * @return boards
     */
    public synchronized ClientBoard[] getBoards() {
        return boards;
    }

    /**
     * Getter for players
     * @return players
     */
    public synchronized ClientPlayer[] getPlayers() {
        return players;
    }

    /**
     * Getter for clouds
     * @return clouds
     */
    public synchronized ClientCloud[] getClouds() {
        return clouds;
    }

    /**
     * Getter for characters
     * @return characters
     */
    public synchronized ClientCharacter[] getCharacters() {
        return characters;
    }

    /**
     * Getter for myId
     * @return myId
     */
    public int getMyId() {
        return myId;
    }

    /**
     * Getter for nickname
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for otherPlayerAssistant
     * @return otherPlayerAssistant
     */
    public Map<ClientPlayer, Integer> getOtherPlayerAssistant() {
        return otherPlayerAssistant;
    }

    /**
     * Getter for win
     * @return win
     */
    public WinMessage getWin() {
        return win;
    }

    /**
     * Setter for win
     * @param win WinMessage to set
     */
    public void setWin(WinMessage win) {
        this.win = win;
    }

    /**
     * Setter for gameModel
     * @param gameModel ClientGameModel to set
     */
    public void setGameModel(ClientGameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * Setter for currentCharacter
     * @param currentCharacter Optional<Integer> to set
     */
    public void setCurrentCharacter(Optional<Integer> currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    /**
     * Setter for error
     * @param error ErrorType to set
     */
    public void setError(ErrorMessage.ErrorType error) {
        this.error = error;
    }

    /**
     * Setter for myId
     * @param myId int to set
     */
    public void setMyId(int myId) {
        this.myId = myId;
    }

    /**
     * Setter for turn
     * @param turn TurnMessage to set
     */
    public void setTurn(TurnMessage turn){
        this.turn = turn;
    }

    /**
     * Setter for otherPlayerAssistant
     * @param otherPlayerAssistant Map<ClientPlayer,Integer> to set
     */
    public void setOtherPlayerAssistant(Map<ClientPlayer, Integer> otherPlayerAssistant) {
        this.otherPlayerAssistant = otherPlayerAssistant;
    }
}

