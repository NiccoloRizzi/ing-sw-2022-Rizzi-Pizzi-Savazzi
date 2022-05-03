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
                PlayerConnection connection = new PlayerConnection(socket);
                executor.submit(()->{
                    boolean added = false;
                    boolean uniqueNickname = true;
                    connection.receiveInfo();
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
                                if (lobby.getNumOfPlayer() = connection.getNumOfPlayer() && lobby.isMode() = connection.isExpertMode() && !lobby.isStarted()) {
                                    added = lobby.addPlayer(connection);
                                }
                            }
                            if (!added) {
                                Lobby lobby = new Lobby(connection.getNumOfplayer(), connection.isExperMode(), this);
                                lobby.addPlayer(connection);
                                lobbies.add(l);
                            }
                        }
                    }
                });
            } catch (IOException e){
                System.err.println("Connection error!");
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
