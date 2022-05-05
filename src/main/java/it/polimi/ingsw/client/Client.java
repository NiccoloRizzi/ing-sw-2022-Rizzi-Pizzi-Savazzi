package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private boolean isActive;
    private final View view;

    public Client(String IP, int port) throws IOException {
        socket = new Socket(IP, port);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
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
            try {
                while (isActive) {
                    String read = (String) in.readObject();
                    System.out.println(read);
                    ClientModel model = ClientModelDeSerializer.deserialize(read);
                    model.accept(view);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Server closed");
                e.printStackTrace();
            }
        });
        t.start();
        return t;
    }

    public void writeToSocket(String message) {
        Thread t = new Thread(() -> {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
