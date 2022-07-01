package it.polimi.ingsw.server;

import it.polimi.ingsw.clientModels.Answers.ErrorMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class handling game server
 */
public class Server {
    /**
     * The port of the server
     */
    private final int PORT;
    /**
     * The socket of the server
     */
    private ServerSocket serverSocket;
    /**
     * The list of active lobbies on the server
     */
    private final ArrayList<Lobby> lobbies;
    /**
     * The executor for threads launcher by the server
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(128);

    /**
     * Creates a server and makes it available
     */
    public Server(int PORT){
        this.PORT = PORT;
        lobbies = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main thread method that listens for connecting players
     */
    public void run(){
        System.out.println("Server listening on port: " + PORT);
        while(true){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Player connected.");
                PlayerConnection connection = new PlayerConnection(socket,this);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    /**
     *
     * @return The list of active lobbies
     */
    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * Adds a playerConnection to a lobby
     * @param connection The playerConnection
     */
    public synchronized void addToLobby(PlayerConnection connection){
        boolean added = false;
        boolean uniqueNickname = true;
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getNicknames().contains(connection.getNickname())) {
                    connection.update(new ErrorMessage(0, ErrorMessage.ErrorType.NicknameTaken));
                    connection.closeConnection();
                    uniqueNickname = false;
                }
            }
            if (uniqueNickname) {

                for (Lobby lobby : lobbies) {
                    if (lobby.getNumOfPlayer() == connection.getNumOfPlayers() && lobby.isExpertMode() == connection.isExpertMode() && !lobby.isStarted()) {
                        System.out.println("Adding player "+connection.getNickname()+" to lobby "+lobbies.indexOf(lobby));
                        added = lobby.addPlayer(connection);
                    }
                }
                if (!added) {
                    Lobby lobby = new Lobby(connection.getNumOfPlayers(), connection.isExpertMode(), this);
                    lobbies.add(lobby);
                    System.out.println("Created lobby "+lobbies.indexOf(lobby));
                    System.out.println("Adding player "+connection.getNickname()+" to lobby "+lobbies.indexOf(lobby));
                    lobby.addPlayer(connection);
                }
            }
        }
    }

    /**
     * Removes a lobby from lobbies list
     * @param lobby The lobby to be removed
     */
    public void removeLobby(Lobby lobby)
    {
        synchronized (lobbies) {
            lobbies.remove(lobby);
        }
    }
}
