package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.clientModels.ClientModelDeSerializer;
import it.polimi.ingsw.server.Observer;
import it.polimi.ingsw.client.cli.Cli;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Observer<JsonObject>{
    /**
     * Socket to connect to the server
     */
    private Socket socket;
    /**
     * Scanner to read message sent by the server
     */
    private Scanner in;
    /**
     * PrintWriter to write message to the server
     */
    private PrintWriter out;
    /**
     * Boolean to check if the server connection is still active
     */
    private boolean isActive=false;
    /**
     * View of the game
     */
    private View view;
    /**
     * Nickname of the player
     */
    private String nickname;
    /**
     * Id of the player assigned by the server
     */
    private int id;
    /**
     * Gson used to deserialize the type of the message
     */
    private final static Gson gson = new Gson();

    /**
     * Method to start a connection with the server
     * @param IP server IP
     * @param port server Port
     * @throws IOException can be thrown when creating the socket
     */
    public void startConnection(String IP, int port) throws IOException{
        socket = new Socket(IP, port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        isActive = true;
        // Run();
        readFromSocket();
    }

    /**
     * Setter for the nickname
     * @param nickname string with the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Getter for the nickname
     * @return nickname
     */
    public String getNickname(){
        return nickname;
    }

    /**
     * Setter fo the view
     * @param view View to set
     */
    public void setView(View view){
        this.view = view;
        view.addObserver(this);
    }

    /**
     * Method to read from the socket and handle the received messages
     */
    public void readFromSocket(){
        Thread t = new Thread(() -> {
            while (isActive) {
                if(in.hasNext()){
                    String read = in.nextLine();
                    JsonObject jo = gson.fromJson(read,JsonObject.class);
                    System.out.println(read);
                    if(jo.get("type").getAsString().equals("ping")){
                        JsonObject answer = new JsonObject();
                        answer.addProperty("type","pong");
                        writeToSocket(answer.toString());
                    } else if (jo.get("type").getAsString().equals("disconnect")) {
                        isActive = false;
                    } else {
                        System.out.println(read);
                        ClientModel model = ClientModelDeSerializer.deserialize(read);
                        model.accept(view);
                    }
                }
            }
            System.out.println("closed");
        });
        t.start();
    }

    /**
     * Method to write a message to the socket
     * @param message message to write
     */
    public void writeToSocket(String message) {
        Thread t = new Thread(() -> {
            out.println(message);
            out.flush();
        });
        t.start();
    }

    /**
     * Method to create a CLI view
     */
    public void startCli(){
        this.view = new Cli();
        view.addObserver(this);
        view.start();
    }

    /**
     * Getter fot the ID
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter for the ID
     * @param id ID to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Method that handles the message received from the observed object
     * @param command message received as JsonObject
     */
    @Override
    public void update(JsonObject command) {
        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(command,JsonObject.class);
        System.out.println("connecting...");
        if(jo.get("command").getAsString().equals("message")){
            writeToSocket(command.toString());
        }
        else {
            if (jo.get("command").getAsString().equals("connect")) {
                    new Thread(() ->
                    {
                        try {
                            startConnection(jo.get("ip").getAsString(), jo.get("port").getAsInt());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();
            }
            if(jo.get("command").getAsString().equals("disconnect")){
                close();
            }
        }
    }

    /**
     * Method to close the socket and the scanner when the client has to be closed
     */
    public void close()
    {
        if(isActive && !socket.isClosed()) {
            isActive = false;
            in.close();
            out.close();
            try {
                socket.close();
                System.out.println("Client closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
