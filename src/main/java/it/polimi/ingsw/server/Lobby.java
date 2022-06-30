package it.polimi.ingsw.server;

import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.exceptions.PlayerOutOfBoundException;

import java.util.ArrayList;

/**
 * Class for handling server lobbies
 */
public class Lobby {
    /**
     * PlayerConnections of players in the lobby
     */
    private final ArrayList<PlayerConnection> players;
    /**
     * The number of players for this lobby
     */
    private final int numOfPlayer;
    /**
     * Whether the lobby is for an expert mode match
     */
    private final boolean expertMode;
    /**
     * The server containing the lobby
     */
    private final Server server;
    /**
     * Whether the game has already started for this lobby
     */
    private boolean started;

    /**
     * Creates a lobby with given setup parameters
     * @param numOfPlayer Number of players for the match
     * @param mode Whether the match will be in expert mode
     * @param server The server the lobby is created in
     */
    public Lobby (int numOfPlayer,boolean mode, Server server)
    {
        this.numOfPlayer=numOfPlayer;
        this.expertMode = mode;
        this.server = server;
        started = false;
        players=new ArrayList<>();
    }

    /**
     *
     * @return Whether the game has started
     */
    public synchronized boolean isStarted() {
        return started;
    }

    /**
     *
     * @return The number of players
     */
    public int getNumOfPlayer() {
        return numOfPlayer;
    }

    /**
     *
     * @return Whether it is an expert mode lobby
     */
    public boolean isExpertMode() {
        return expertMode;
    }

    /**
     * Adds a player to the lobby
     * @param player The playerConnection of the player being added
     * @return Whether the addition was successful
     */
    public synchronized boolean addPlayer(PlayerConnection player) {
        if(players.size() < numOfPlayer) {
            players.add(player);
            player.setLobby(this);
            if (players.size() == numOfPlayer)
                startGame();

            return true;
        }
        return false;
    }
    /**
     * Starts the match for this lobby
     */
    public synchronized void startGame()
    {
        System.out.println("Starting game of lobby "+server.getLobbies().indexOf(this));
        started = true;
        notifyAll();
        Game game = new Game(numOfPlayer, expertMode);
        MessageVisitor messageVisitor = new MessageVisitor(game);
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

    /**
     * Kicks players and removes the lobby from the server
     */
    public synchronized void closeLobby()
    {
        for(PlayerConnection player: players){
            player.closeConnection();
        }
        server.removeLobby(this);
    }

    /**
     * Handles a player disconnecting by closing the lobby
     * @param player The playerConnection of the disconnected player
     */
    public void deregister(PlayerConnection player)
    {
        for(PlayerConnection p: players){
            if(!p.equals(player))
             p.update(new ErrorMessage(players.indexOf(player), ErrorMessage.ErrorType.PlayerDisconnected));
        }
        closeLobby();
    }


    /**
     *
     * @return The list of nicknames of players in the lobby
     */
    public ArrayList<String> getNicknames()
    {
        ArrayList<String> temp = new ArrayList<>();
        for(PlayerConnection player: players) {
            temp.add(player.getNickname());
        }
        return temp;
    }
}