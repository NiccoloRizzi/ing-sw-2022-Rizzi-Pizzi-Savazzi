package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class GUI extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Client client = new Client(false);
        loader.setLocation(getClass().getResource("/serverConnection.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Eryantis");
        ViewGui view = loader.getController();
        client.setView(view);

        stage.show();
    }
}
