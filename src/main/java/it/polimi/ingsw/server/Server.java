package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT= 12345;
    private ServerSocket serverSocket;
    private ArrayList<Lobby> lobbies;
    private ExecutorService executor = Executors.newFixedThreadPool(128);

    public void run(){
        System.out.println("Server listening on port: " + PORT);
        while(true){
            try {
                Socket socket = serverSocket.accept();
                PlayerConnection connection = new PlayerConnection(socket,this);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

    public void addToLobby(PlayerConnection connection){
        boolean added = false;
        boolean uniqueNickname = true;
        synchronized (lobbies) {
            for (Lobby lobby : lobbies) {
                if (lobby.getNicknames().contains(connection.getNickname())) {
//                              connection.send(); nickname già preso
                    connection.closeConnection();
                    uniqueNickname = false;
                }
            }
            if (uniqueNickname) {
                for (Lobby lobby : lobbies) {
                    if (lobby.getNumOfPlayer() == connection.getNumOfPlayers() && lobby.isExpertMode() == connection.isExpertMode() && !lobby.isStarted()) {
                        added = lobby.addPlayer(connection);
                    }
                }
                if (!added) {
                    Lobby lobby = new Lobby(connection.getNumOfPlayers(), connection.isExpertMode(), this);
                    lobby.addPlayer(connection);
                    lobbies.add(lobby);
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
