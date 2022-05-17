package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;

public class ModelView {
    private ClientPlayer[] players;
    private ClientGameModel gameModel;
    private ClientBoard[] boards;
    private ClientCloud[] clouds;
    private ClientCharacter[] characters;
    private ErrorMessage.ErrorType error;
    private TurnMessage.Turn turn;

    public ModelView(int nplayers, boolean expertMode){
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

    public void setTurn(TurnMessage.Turn turn){
        this.turn = turn;
    }
}

