package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.GUI.ViewGui;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.clientModels.ClientModelDeSerializer;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.server.Observer;
import it.polimi.ingsw.client.cli.Cli;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Observer<JsonObject>{
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private boolean isActive=true;
    private int playersNumber;
    private View view;
    private boolean expert;
    private String nickname;
    private int id;
    private static Gson gson = new Gson();

    public void startConnection(String IP, int port) throws IOException{
        socket = new Socket(IP, port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        try {
            run();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public Client(boolean cli){
        if(cli) {
            startCli();
        }
    }

    public void setView(View view){
        this.view = view;
        view.addObserver(this);
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }

    public void setExpert(boolean expert) {
        this.expert = expert;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
    public int getPlayersNumber(){
        return playersNumber;
    }
    public Thread readFromSocket(){
        Thread t = new Thread(() -> {
            while (isActive) {
                String read = in.nextLine();
                JsonObject jo = gson.fromJson(read,JsonObject.class);
                if(jo.get("type").getAsString().equals("ping")){
                    JsonObject answer = new JsonObject();
                    answer.addProperty("type","pong");
                    writeToSocket(answer.toString());
                }
                else {
                    ClientModel model = ClientModelDeSerializer.deserialize(read);
                    model.accept(view);
                }
            }
        });
        t.start();
        return t;
    }

    public boolean isExpert() {
        return expert;
    }

    public void writeToSocket(String message) {
        Thread t = new Thread(() -> {
            out.println(message);
            out.flush();
        });
        t.start();
    }

    public void startCli(){
        this.view = new Cli();
        view.addObserver(this);
        view.start();
    }

    public void run() throws IOException {
        try {
            Thread readThread = readFromSocket();
            readThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            socket.getInputStream().close();
            socket.getOutputStream().close();
            socket.close();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void update(JsonObject command) {
        Gson gson = new Gson();
        JsonObject jo = gson.fromJson(command,JsonObject.class);
        System.out.println("connecting...");
        if(jo.get("command").getAsString().equals("message")){
            writeToSocket(command.toString());
        }
        else if(jo.get("command").getAsString().equals("connect")){
                new Thread ( () ->
                {
                    try {
                        startConnection(jo.get("ip").getAsString(),jo.get("port").getAsInt());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
        }
    }


}
