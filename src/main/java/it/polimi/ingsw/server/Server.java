package it.polimi.ingsw.server;

import javax.crypto.spec.PSource;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT= 12345;
    private ServerSocket serverSocket;
    private final ArrayList<Lobby> lobbies;
    private final ExecutorService executor = Executors.newFixedThreadPool(128);

    public Server(){
        lobbies = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public ArrayList<Lobby> getLobbies() {
        return lobbies;
    }

    public synchronized void addToLobby(PlayerConnection connection){
        boolean added = false;
        boolean uniqueNickname = true;
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getNicknames().contains(connection.getNickname())) {
                    System.out.println("Nickname already taken.");
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

    public void removeLobby(Lobby lobby)
    {
        synchronized (lobbies) {
            lobbies.remove(lobby);
        }
    }
}
