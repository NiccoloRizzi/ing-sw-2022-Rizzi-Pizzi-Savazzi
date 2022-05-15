package it.polimi.ingsw.server;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.exceptions.EmptyMessageException;
import it.polimi.ingsw.messages.Message;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class PlayerConnection implements Runnable, Observer<ClientModel> {
    private final Server server;
    private final Socket socket;
    private String nickname;
    private int numOfPlayers;
    private boolean expertMode;
    private PrintWriter out;
    private boolean active;
    private MessageVisitor mv;
    private Lobby lobby;
    private boolean verified = true;

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public boolean isExpertMode() {
        return expertMode;
    }

    public PlayerConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        active = true;
        try {
            out = new PrintWriter(socket.getOutputStream());
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
            out.println(answer);
            out.flush();
    }

    public synchronized void closeConnection(){
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        active = false;
    }
    public void connectionChecker(){
        Random random = new Random();
        Gson gson = new Gson();
        String json;
        while(isActive()) {
            JsonObject jo = new JsonObject();
            jo.addProperty("type","ping");
            send(jo.toString());
            verified = false;
            try {
                TimeUnit.SECONDS.sleep(10);
                if (!verified) {
                    System.out.println(nickname + " disconnected.");
                    closeConnection();
                }
                TimeUnit.SECONDS.sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public void run(){
        String read;
        Scanner in;
        int i=0;
        try{
            in = new Scanner(socket.getInputStream());
            read = in.nextLine();
            Gson gson = new Gson();
            JsonObject message = gson.fromJson(read, JsonObject.class);
            if(message.get("type").getAsString().equals("PlayerMessage")) {
                nickname = message.get("nickname").getAsString();
                numOfPlayers = message.get("playersNumber").getAsInt();
                expertMode = message.get("expertMode").getAsBoolean();
                server.addToLobby(this);
            }
            synchronized (lobby) {
                while (!lobby.isStarted()) {
                    try {
                        lobby.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            new Thread(()->connectionChecker()).start();
            while(isActive()){
                read = in.nextLine();
                message = gson.fromJson(read,JsonObject.class);
                if(message.get("type").getAsString().equals("pong")) {
                    verified = true;
                }else {
                    Message actionMessage = MoveSerializer.deserialize(read);
                    send(actionMessage);
                    actionMessage.accept(mv);
                }
            }
            closeConnection();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(ClientModel message) {
            send(message.serialize());
    }
}
