package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;

public class View {

    private ClientPlayer[] players;
    private ClientGameModel gameModel;
    private ClientBoard[] boards;
    private ClientCloud[] clouds;
    private ClientCharacter[] characters;

    private int myID;

    public int getMyID() {
        return myID;
    }

    private final Client client;

    public View(Client client){
        this.client = client;
    }

    public synchronized void sendMessage(String messageToSend) {
        client.writeToSocket(messageToSend);
    }

    public ClientGameModel getGameModel() {
        return gameModel;
    }
    public ClientBoard[] getBoards() {
        return boards;
    }
    public ClientPlayer[] getPlayer() {
        return players;
    }
    public ClientCloud[] getClouds() {
        return clouds;
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
    public synchronized void visit(int myID){
        boards = new ClientBoard[4];
        this.myID = myID;
    }
    public synchronized void visit(ClientCharacter character){
        characters[character.getID()] = character;
    }
}
