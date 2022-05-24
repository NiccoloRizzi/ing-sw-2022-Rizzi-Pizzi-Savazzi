package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.View;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewGUI extends View {
    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void start(){}
    @Override
    public void startGame(){

        Platform.runLater(
                () -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameView.fxml"));
                    loader.setController(this);
                    try {
                        Parent root = loader.load();
                        stage.setScene(new Scene(root));
                        stage.setFullScreen(true);
                        stage.show();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
        );

    }

    @Override
    public void refresh() {

    }
}
