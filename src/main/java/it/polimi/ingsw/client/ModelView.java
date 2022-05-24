package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;

public class ModelView {
    private final ClientPlayer[] players;
    private ClientGameModel gameModel;
    private final ClientBoard[] boards;
    private final ClientCloud[] clouds;
    private ClientCharacter[] characters;
    private ErrorMessage.ErrorType error;
    private TurnMessage turn;
    private final String nickname;
    private int myId;
    private boolean isExpert;

    public ErrorMessage.ErrorType getError() {
        return error;
    }

    public TurnMessage getTurn() {
        return turn;
    }

    public boolean isExpert() {
        return isExpert;
    }

    public ModelView(String nickname, int nplayers, boolean expertMode){
        this.nickname = nickname;
        this.isExpert = expertMode;
        if(expertMode){
            characters = new ClientCharacter[3];
        }
        players = new ClientPlayer[nplayers];
        boards = new ClientBoard[nplayers];
        clouds = new ClientCloud[nplayers];
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

    public void setGameModel(ClientGameModel gameModel) {
        this.gameModel = gameModel;
    }

    public void setError(ErrorMessage.ErrorType error) {
        this.error = error;
    }

    public int getMyId() {
        return myId;
    }

    public void setMyId(int myId) {
        this.myId = myId;
    }

    public void setTurn(TurnMessage turn){
        this.turn = turn;
    }

    public String getNickname() {
        return nickname;
    }
}

