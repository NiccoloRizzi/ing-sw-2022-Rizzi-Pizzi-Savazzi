package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.clientModels.ClientModelDeSerializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket;
    private final Scanner in;
    private final PrintWriter out;
    private boolean isActive;
    private final View view;

    public Client(String IP, int port) throws IOException {
        socket = new Socket(IP, port);
        in = new Scanner(socket.getInputStream());
        out = new PrintWriter(socket.getOutputStream());
        view = new View(this);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Thread readFromSocket(){
        Thread t = new Thread(() -> {
            System.out.println("Thread creato!");
            while (isActive) {
                String read = in.nextLine();
                System.out.println(read);
                ClientModel model = ClientModelDeSerializer.deserialize(read);
                model.accept(view);
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
