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
                executor.submit(()->{
                    boolean added = false;
                    connection.receiveInfo();
                    synchronized (lobbies) {
                        for (Lobby lobby : lobbies) {
                            if (lobby.getNumOfPlayer() = connection.getNumOfPlayer() && lobby.isMode() = connection.isMode()&& !lobby.isStarted()) {
                                added = lobby.addPlayer(connection);
                            }
                        }
                        if (!added) {
                            Lobby lobby = new Lobby(connection.getNumOfplayer(), connection.isMode(), this);
                            lobby.addPlayer(connection);
                            lobbies.add(l);
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
