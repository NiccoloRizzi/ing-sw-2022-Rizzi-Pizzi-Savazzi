package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.clientModels.ClientModelDeSerializer;
import it.polimi.ingsw.messages.PlayerMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;
    private boolean isActive=true;
    private final View view;
    private String nickname;
    private int pn;
    private boolean expert;

    public Client(String IP, int port) throws IOException {
        socket = new Socket(IP, port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        view = new View(this);
    }

    public Client(){
        socket = null;
        in = null;
        out = null;
        view = null;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPn(int pn) {
        this.pn = pn;
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

    public Thread readFromSocket(){
        Thread t = new Thread(() -> {
            Gson gson = new Gson();
            System.out.println("Thread creato!");
            writeToSocket(MessageSerializer.serialize(new PlayerMessage(nickname,pn,expert)));
            System.out.println("Message sent");
            while (isActive) {
                String read = in.nextLine();
                JsonObject jo = gson.fromJson(read,JsonObject.class);
                if(jo.get("type").getAsString().equals("ping")){
                    System.out.println("Pong!");
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
}
