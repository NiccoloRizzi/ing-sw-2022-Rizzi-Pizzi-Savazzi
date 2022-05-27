package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
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
        loader1.setLocation(getClass().getResource("/gameView.fxml"));
        stage.setTitle("Eryantis");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setScene(new Scene(loader1.load()));
        System.out.println(stage.getHeight());
        System.out.println(stage.getWidth());
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images\\logo.png")));
        ViewGUI view = loader1.getController();
        view.setStage(stage);
        client.setView(view);
        stage.show();
        stage.setOnCloseRequest(event-> {event.consume();exit(stage);});

        FXMLLoader loader2 = new FXMLLoader();
        Stage connectionStage = new Stage();
        loader2.setLocation(getClass().getResource("/serverConnection.fxml"));
        Scene scene = new Scene(loader2.load());
        ((VBox)scene.getRoot()).setBackground(new Background(new BackgroundImage(new Image("/images\\background1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
        connectionStage.setScene(scene);
        connectionStage.setTitle("connection to server");
        connectionStage.getIcons().add(new Image(getClass().getResourceAsStream("/images\\logo.png")));
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
