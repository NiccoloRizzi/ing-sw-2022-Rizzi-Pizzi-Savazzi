package it.polimi.ingsw.server;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.controller.MessageVisitor;
import it.polimi.ingsw.messages.Message;
import it.polimi.ingsw.messages.PlayerMessage;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Class for handling a player's connection
 */
public class PlayerConnection implements Runnable, Observer<ClientModel> {
    /**
     * The server
     */
    private final Server server;
    /**
     * The socket of the player
     */
    private final Socket socket;
    /**
     * The nickname chosen by the player
     */
    private String nickname;
    /**
     * The size of the game the player wants to be a part of
     */
    private int numOfPlayers;
    /**
     * Whether the player wants to play in an expert mode match
     */
    private boolean expertMode;
    /**
     * The PrintWriter for sending updates and answers to the player
     */
    private PrintWriter out;
    /**
     * Whether the player is active and responding to pings
     */
    private boolean active;
    /**
     * The message visitor that handles this player's moves
     */
    private MessageVisitor mv;
    /**
     * The lobby the player's in
     */
    private Lobby lobby;
    /**
     * Whether the player has answered last ping
     */
    private boolean verified = true;

    /**
     *
     * @return The size of the game the player wants to be a part of
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     *
     * @return Whether the player wants to play in an expert mode match
     */
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

    /**
     *
     * @return The nickname chosen by the player
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Sets the current lobby for the player
     * @param lobby The lobby the player has been added to
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    /**
     * Sets a nickname for the player
     * @param nickname The chosen nickname
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     *
     * @return Whether the player is active and answering pings
     */
    public synchronized boolean isActive(){
        return active;
    }

    /**
     * Sets the message visitor that will handle player's moves
     * @param mv The message visitor
     */
    public synchronized void setMessageVisitor(MessageVisitor mv){
        this.mv = mv;
    }

    /**
     * Sends an answer to the player
     * @param answer The answer
     */
    public synchronized void send(Object answer){
        out.println(answer);
        out.flush();
    }


    /**
     * Closes connection with the player
     */
    public void closeConnection(){
        JsonObject disconnected = new JsonObject();
        disconnected.addProperty("type","disconnect");
        send(disconnected.toString());
        active=false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Keeps sending ping packets to make sure the player is still connected
     */
    public void connectionChecker(){
        while(isActive()) {
            JsonObject jo = new JsonObject();
            jo.addProperty("type","ping");
            send(jo.toString());
            verified = false;
            try {
                TimeUnit.SECONDS.sleep(10);
                if (!verified) {
                    System.out.println(nickname + " disconnected.");
                    lobby.deregister(this);
                }
                TimeUnit.SECONDS.sleep(10);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Main thread that handles incoming player's messages
     */
    public void run(){
        String read;
        Scanner in;
        Gson gson = new Gson();
        try{
            in = new Scanner(socket.getInputStream());
            read = in.nextLine();
            try{
                PlayerMessage playerMessage = MoveDeserializer.deserializePlayerMessage(read);
                nickname = playerMessage.getNickname();
                numOfPlayers = playerMessage.getPlayersNumber();
                expertMode = playerMessage.isExpertMode();
                server.addToLobby(this);
                new Thread(this::connectionChecker).start();
            }catch (IllegalArgumentException ignored){}
            while(isActive()){
                read = in.nextLine();
                System.out.println(nickname +": " +read);
                JsonObject message = gson.fromJson(read,JsonObject.class);
                if(message.get("type").getAsString().equals("pong")) {
                    verified = true;
                }else if(lobby.isStarted()) {
                    Message actionMessage = MoveDeserializer.deserialize(read);
                    actionMessage.accept(mv);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(ClientModel message) {
            send(message.serialize());
    }
}
