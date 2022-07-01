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
import java.util.Objects;

public class GUI extends Application{

    /**
     * The client
     */
    private static Client client;

    public static void main(String[] args){
        launch();
    }
    /**
     * Method to create the stage for the game and the stage for the connection to the server
     * @param stage the default stage created by javaFX
     * @throws IOException can be thrown by javaFX loader when loading a scene
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader1 = new FXMLLoader();
        client = new Client();
        loader1.setLocation(getClass().getResource("/gameView.fxml"));
        stage.setTitle("Eryantis");
        stage.setMaximized(true);
        //stage.setFullScreen(true);
        AnchorPane root = loader1.load();
        root.setBackground(new Background(new BackgroundImage(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/background.png"))), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
        stage.setScene(new Scene(root));
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/logo.png"))));
        ViewGUI view = loader1.getController();
        view.setStage(stage);
        client.setView(view);
        stage.show();
        stage.setOnCloseRequest(event-> {event.consume();exit(stage);});

        FXMLLoader loader2 = new FXMLLoader();
        Stage connectionStage = new Stage();
        loader2.setLocation(getClass().getResource("/serverConnection.fxml"));
        Scene scene = new Scene(loader2.load());
        ((VBox)scene.getRoot()).setBackground(new Background(new BackgroundImage(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/background1.png"))), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
        connectionStage.setScene(scene);
        connectionStage.setResizable(false);
        connectionStage.setTitle("connection to server");
        connectionStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/logo.png"))));
        connectionStage.alwaysOnTopProperty();
        ((ConnectionController)loader2.getController()).setView(view);
        ((ConnectionController)loader2.getController()).setClient(client);
        connectionStage.initModality(Modality.APPLICATION_MODAL);
        connectionStage.setOnCloseRequest(event-> {event.consume();exitConnection(connectionStage,stage);});
        connectionStage.show();
    }

    /**
     * Method to close one stage and the client
     * @param stage stage that ha sto be closed
     */
    public void exit(Stage stage){
        new Thread(()-> client.close()).start();
        stage.close();
    }

    /**
     * Method to close the client and two stages
     * @param stage1 the first stage that has to be closed
     * @param stage2 the second stage that has to be closed
     */
    public void exitConnection(Stage stage1,Stage stage2){
        new Thread(()-> client.close()).start();
        stage1.close();
        stage2.close();
    }
}
