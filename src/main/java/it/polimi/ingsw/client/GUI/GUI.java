package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends Application{

    private Client client;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader1 = new FXMLLoader();
        client = new Client(false);
        loader1.setLocation(getClass().getResource("/waitingServer.fxml"));
        stage.setScene(new Scene(loader1.load()));
        stage.setTitle("Eryantis");
        stage.setFullScreen(true);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images\\Moneta_base.png")));
        ViewGUI view = loader1.getController();
        view.setStage(stage);
        client.setView(view);
        stage.show();
        stage.setOnCloseRequest(event-> {event.consume();exit(stage);});

        FXMLLoader loader2 = new FXMLLoader();
        Stage connectionStage = new Stage();
        loader2.setLocation(getClass().getResource("/serverConnection.fxml"));
        connectionStage.setScene(new Scene(loader2.load()));
        connectionStage.setTitle("connection to server");
        connectionStage.getIcons().add(new Image(getClass().getResourceAsStream("/images\\Moneta_base.png")));
        connectionStage.alwaysOnTopProperty();
        ((ConnectionController)loader2.getController()).setView(view);
        connectionStage.initModality(Modality.APPLICATION_MODAL);
        connectionStage.setOnCloseRequest(event-> {event.consume();exitConnection(connectionStage,stage);});
        connectionStage.show();
    }

    public void exit(Stage stage){
        client.close();
        stage.close();
    }

    public void exitConnection(Stage stage1,Stage stage2){
        client.close();
        stage1.close();
        stage2.close();
    }
}
