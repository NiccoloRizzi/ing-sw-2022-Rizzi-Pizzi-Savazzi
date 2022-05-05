package it.polimi.ingsw.server;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.controller.Game;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.messages.Message;

import java.io.*;
import java.net.Socket;
import java.util.EventListener;
import java.util.Scanner;

public class PlayerConnection implements Runnable, EventListener {
    private Server server;
    private Socket socket;
    private String nickname;
    private int numOfPlayers;
    private boolean expertMode;
    private ObjectOutputStream out;
    private boolean active;
    private MessageVisitor mv;
    private Lobby lobby;

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public PlayerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public String getNickname(){
        return nickname;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setMessageVisitor(MessageVisitor mv){
        this.mv = mv;
    }

    public synchronized void send(Object answer){
        try{
            out.reset();
            out.writeObject(answer);
            out.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void closeConnection(){
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        active = false;
    }

    public void run(){
        String read;
        Scanner in;
        try{
            in = new Scanner(socket.getInputStream());
            read = in.nextLine();
            Gson gson = new Gson();
            JsonObject player = gson.fromJson(read, JsonObject.class);
            nickname = player.get("nickname").getAsString();
            numOfPlayers = player.get("players").getAsInt();
            expertMode = player.get("expert").getAsBoolean();
            server.addToLobby(this);
            while(!lobby.isStarted()){
                try {
                    wait();
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            while(isActive()){
                read = in.nextLine();
                Message message = MoveSerializer.deserialize(read);
                message.accept(mv);
            }
            closeConnection();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

}
