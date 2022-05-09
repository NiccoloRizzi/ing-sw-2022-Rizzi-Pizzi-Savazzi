package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.ArrayList;

public class View {

    private ClientBoard board;
    private ClientPlayer player;
    private ArrayList<ClientIsle> isle;
    private final Client client;

    public View(Client client){
        this.client = client;
    }

    public synchronized void sendMessage(String messageToSend) {
        client.writeToSocket(messageToSend);
    }

    public ClientBoard getBoard() {
        return board;
    }
    public ClientPlayer getPlayer() {
        return player;
    }
    public ArrayList<ClientIsle> getIsle() {
        return isle;
    }

    public synchronized void visit(ClientBoard clientBoard){
        board = clientBoard;
        System.out.println(board.getEntrance());
    }
    public synchronized void visit(ClientIsle clientIsle){
        isle.set(clientIsle.getId(), clientIsle);
    }
    public synchronized void visit(ClientPlayer clientPLayer){
        player = clientPLayer;
    }
}
