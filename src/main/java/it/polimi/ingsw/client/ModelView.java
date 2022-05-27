package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelView {
    private final ClientPlayer[] players;
    private ClientGameModel gameModel;
    private final ClientBoard[] boards;
    private final ClientCloud[] clouds;
    private ClientCharacter[] characters;
    private Optional<Integer> currentCharacter;
    private Map<ClientPlayer, Integer> otherPlayerAss;
    private ErrorMessage.ErrorType error;
    private TurnMessage turn;
    private WinMessage win;
    private final String nickname;
    private int myId;
    private final boolean isExpert;

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
        otherPlayerAss = new HashMap<>();
    }

    public ErrorMessage.ErrorType getError() {
        return error;
    }
    public TurnMessage getTurn() {
        return turn;
    }
    public Optional<Integer> getCurrentCharacter() {
        return currentCharacter;
    }
    public boolean isExpert() {
        return isExpert;
    }
    public synchronized ClientGameModel getGameModel() {
        return gameModel;
    }
    public synchronized ClientBoard[] getBoards() {
        return boards;
    }
    public synchronized ClientPlayer[] getPlayers() {
        return players;
    }
    public synchronized ClientCloud[] getClouds() {
        return clouds;
    }
    public synchronized ClientCharacter[] getCharacters() {
        return characters;
    }
    public int getMyId() {
        return myId;
    }
    public String getNickname() {
        return nickname;
    }
    public Map<ClientPlayer, Integer> getOtherPlayerAss() {
        return otherPlayerAss;
    }
    public WinMessage getWin() {
        return win;
    }

    public void setWin(WinMessage win) {
        this.win = win;
    }
    public void setGameModel(ClientGameModel gameModel) {
        this.gameModel = gameModel;
    }
    public void setCurrentCharacter(Optional<Integer> currentCharacter) {
        this.currentCharacter = currentCharacter;
    }
    public void setError(ErrorMessage.ErrorType error) {
        this.error = error;
    }
    public void setMyId(int myId) {
        this.myId = myId;
    }
    public void setTurn(TurnMessage turn){
        this.turn = turn;
    }
    public void setOtherPlayerAss(Map<ClientPlayer, Integer> otherPlayerAss) {
        this.otherPlayerAss = otherPlayerAss;
    }
}

