package it.polimi.ingsw.server;

import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.exceptions.PlayerOutOfBoundException;

import java.net.Socket;
import java.util.ArrayList;

public class Lobby {
    private final ArrayList<PlayerConnection> players;
    private final int numOfPlayer;
    private final boolean expertMode;
    private final Server server;
    private Game game;
    private MessageVisitor messageVisitor;
    private boolean started;

    public Lobby (int numOfPlayer,boolean mode, Server server)
    {
        this.numOfPlayer=numOfPlayer;
        this.expertMode = mode;
        this.server = server;
        started = false;
        players=new ArrayList<>();
    }

    public synchronized boolean isStarted() {
        return started;
    }

    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public synchronized boolean addPlayer(PlayerConnection player) {
//        for(PlayerConnection p: players){
//            if(!p.isActive())
//            {
//                closeLobby();
//                return false;
//            }
//        }
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
        System.out.println("Starting game of lobby "+server.getLobbies().indexOf(this));
        started = true;
        notifyAll();
        game = new Game(numOfPlayer,expertMode);
        messageVisitor = new MessageVisitor(game);
        for(PlayerConnection player: players){
            try {
                game.createPlayer(player.getNickname());
            }catch(PlayerOutOfBoundException e){
                e.printStackTrace();
            }
            player.setMessageVisitor(messageVisitor);
        }
        game.setupGame();
        for(PlayerConnection player: players)
        {
            game.addObserver(player);
            messageVisitor.addObserver(player);
        }
        game.sendInitialGame();
        StartMessage start = new StartMessage(game.getGameModel().getPlayers());
        for(PlayerConnection player: players){
            player.update(start);
        }
    }

    public synchronized void closeLobby()
    {
        for(PlayerConnection player: players){
            player.closeConnection();
        }
        server.removeLobby(this);
    }

    public void deregister(PlayerConnection player)
    {
        for(PlayerConnection p: players){
            if(!p.equals(player))
             player.update(new ErrorMessage(players.indexOf(player), ErrorMessage.ErrorType.PlayerDisconnected));
        }
        closeLobby();
    }

    public void gameEnd(){
        for(PlayerConnection p: players){
      //      if(!p.equals(player))
//              player.send(); messaggio vittoria
        }
        closeLobby();
    }

    public ArrayList<String> getNicknames()
    {
        ArrayList<String> temp = new ArrayList<>();
        for(PlayerConnection player: players) {
            temp.add(player.getNickname());
        }
        return temp;
    }
}