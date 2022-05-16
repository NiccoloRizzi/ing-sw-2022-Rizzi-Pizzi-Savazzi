package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.clientModels.ClientModelDeSerializer;
import it.polimi.ingsw.messages.*;
import it.polimi.ingsw.model.Colour;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;
    private boolean isActive=true;
    private View view;
    private int pn;
    private boolean expert;
    private String nickname;


    public Client(String IP, int port) throws IOException {
        socket = new Socket(IP, port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void startView(){
        view = new View(this);
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
    public int getPlayersNumber(){
        return pn;
    }
    public Thread readFromSocket(){
        Thread t = new Thread(() -> {
            Gson gson = new Gson();
            writeToSocket(MessageSerializer.serialize(new PlayerMessage(nickname,pn,expert)));
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

    public void ChooseAssistant(int assistant){
        AssistantChoiceMessage acm = new AssistantChoiceMessage(assistant-1, view.getMyID());
        writeToSocket(MessageSerializer.serialize(acm));
    }

    public void MoveToIsle(Colour c, int isleid){
        MoveStudentMessage msm = new MoveStudentMessage(view.getMyID(), c, isleid, false);
        writeToSocket(MessageSerializer.serialize(msm));
    }

    public void MoveToTable(Colour c){
        MoveStudentMessage msm = new MoveStudentMessage(view.getMyID(),c,0,true);
        writeToSocket(MessageSerializer.serialize(msm));
    }

    public void ChooseCloud(int cloudid){
        CloudChoiceMessage ccm = new CloudChoiceMessage(view.getMyID(),cloudid);
        writeToSocket(MessageSerializer.serialize(ccm));
    }

    public void MoveMotherNature(int spaces){
        MoveMotherNatureMessage movemnm = new MoveMotherNatureMessage(view.getMyID(),spaces);
        writeToSocket(MessageSerializer.serialize(movemnm));
    }
}
