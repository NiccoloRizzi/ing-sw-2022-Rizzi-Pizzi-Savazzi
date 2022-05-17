package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.clientModels.ClientModelDeSerializer;
import it.polimi.ingsw.messages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private Scanner in;
    private PrintWriter out;
    private boolean isActive=true;
    private int playersNumber;
    private View view;
    private boolean expert;
    private String nickname;
    private int id;


    public Client(View view) {
        this.view = view;
    }

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

    public void setOptions(String nickname, int nplayers, boolean expertMode){
        this.nickname = nickname;
        this.playersNumber = nplayers;
        this.expert = expertMode;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname(){
        return nickname;
    }

    public Client(boolean expert){
        socket = null;
        in = null;
        out = null;
        view = null;
        this.expert = expert;
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
            Gson gson = new Gson();
            writeToSocket(MessageSerializer.serialize(new PlayerMessage(nickname, playersNumber,expert)));
            while (isActive) {
                String read = in.nextLine();
                JsonObject jo = gson.fromJson(read,JsonObject.class);
                if(jo.get("type").getAsString().equals("ping")){
                    JsonObject answer = new JsonObject();
                    answer.addProperty("type","pong");
                    writeToSocket(answer.toString());
                }
                else {
                    System.out.println(read);
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


}
