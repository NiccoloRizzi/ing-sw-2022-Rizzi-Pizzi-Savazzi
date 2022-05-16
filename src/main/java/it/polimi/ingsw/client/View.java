package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;

public class View {

    private ClientPlayer[] players;
    private ClientGameModel gameModel;
    private ClientBoard[] boards;
    private ClientCloud[] clouds;
    private ClientCharacter[] characters;
    private int myID;
    private String nickname;

    private final Client client;

    public View(Client client){
        this.client = client;
        if(client.isExpert()){
            characters = new ClientCharacter[3];
        }
    }

    public synchronized void sendMessage(String messageToSend) {
        client.writeToSocket(messageToSend);
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
    public synchronized int getMyID() {
        return myID;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public synchronized void visit(ClientBoard clientBoard){
        boards[clientBoard.getPlayerID()] = clientBoard;
    }
    public synchronized void visit(ClientIsle clientIsle){
        for(int i = 0; i < gameModel.getIsles().size(); i++){
            if(gameModel.getIsles().get(i).getId() == clientIsle.getId()){
                gameModel.getIsles().set(i, clientIsle);
                break;
            }
        }
    }
    public synchronized void visit(ClientPlayer clientPLayer){
        players[clientPLayer.getId()] = clientPLayer;
    }
    public synchronized void visit(ClientGameModel clientGameModel){
        gameModel = clientGameModel;
    }
    public synchronized void visit(ClientCloud clientCloud){
        clouds[clientCloud.getId()] = clientCloud;
    }
    public synchronized void visit(ClientCharacter character){
        characters[character.getID()] = character;
    }
    public synchronized void visit(ErrorMessage errorMessage){

    }
    public synchronized void visit(TurnMessage turnMessage){

    }
    public synchronized void visit(WinMessage winMessage){

    }
    public synchronized void visit(StartMessage startMessage){
        players = new ClientPlayer[startMessage.getPlayerNumbers()];
        boards = new ClientBoard[startMessage.getPlayerNumbers()];
        clouds = new ClientCloud[startMessage.getPlayerNumbers()];
        myID = startMessage.getPlayer(nickname).getId();
    }
}
