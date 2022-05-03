package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.controller.MessageVisitor;

import java.net.Socket;
import java.util.ArrayList;

public class Lobby {
    private ArrayList<PlayerConnection> players;
    private final int numOfPlayer;
    private final boolean mode;
    private Server server;
    private Game game;
    private MessageVisitor messageVisitor;
    private boolean started;

    public Lobby (int numOfPlayer,boolean mode, Server server)
    {
        this.numOfPlayer=numOfPlayer;
        this.mode = mode;
        this.server = server;
        started = false;
    }

    public boolean isStarted() {
        return started;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public boolean isMode() {
        return mode;
    }

    public synchronized boolean  addPlayer(PlayerConnection player)
    {
        for(PlayerConnection p: players){
            if(!p.checkConnection())
            {
                closeLobby();
                return false;
            }
        }
        if(players.size() < numOfPlayer) {
            players.add(player);
            player.setLobby(this);

            if (players.size() == numOfPlayer)
                startGame();

            return true;
        }
        return false;
    }

    public synchronized void startGame()
    {
        started = true;
        game = new Game(numOfPlayer,mode);
        messageVisitor = new MessageVisitor(game);
        for(PlayerConnection player: players){
            game.createPlayer(PlayerConnection.getNickname());
            player.setMessageVisitor(messageVisitor);
        }
        for(PlayerConnection player: players) {
         //invio messaggio di inizio partita
            new thread(player).start();
        }
    }

    public synchronized void closeLobby()
    {
        for(PlayerConnection player: players){
            player.closeConnection();
        }
        server.removeLobby(this);
    }
}