package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectionController{
    /**
     * The view
     */
    ViewGUI view;
    Client client;
    @FXML
    TextField serverIP;
    @FXML
    TextField serverPort;
    @FXML
    Button connectionButton;
    @FXML
    TextField nickname;
    @FXML
    RadioButton players3,players4;
    @FXML
    CheckBox expert;
    @FXML
    Label error;

    /**
     * Setter for the view
     * @param view View to set
     */
    public void setView(ViewGUI view)
    {
        this.view = view;
    }

    /**
     * Setter for client
     * @param client Client to set
     */
    public void setClient(Client client){
        this.client = client;
    }

    /**
     * Event handler that sends the inserted IP,Port to connect to the server and then change the scene of the stage
     * @param e event that triggered the event handler
     * @throws IOException can be thrown by the javaFX loader when loading the new scene
     */
    public void connect(ActionEvent e) throws IOException
    {
        String ip = serverIP.getText();
        String port = serverPort.getText();
        view.notifyConnection(ip,Integer.parseInt(port));
        if(client.isActive()) {
            view.setIP(ip);
            view.setPort(Integer.parseInt(port));
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/playerInfo.fxml"));
            loader.setController(this);
            Parent root = loader.load();
            ((VBox) root).setBackground(new Background(new BackgroundImage(new Image("images/background1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }else{
            error.setVisible(true);
        }
    }


    /**
     * Event Handler that sends the inserted information to connect to a lobby
     * @param e event that triggered the EventHandler
     */
    public void sendInfo(ActionEvent e)
    {
        String nick = nickname.getText();
        int numOfPlayers = 2;
        if(players3.isSelected())
            numOfPlayers = 3;
        if(players4.isSelected())
            numOfPlayers = 4;
        boolean mode = expert.isSelected();
        view.sendPlayerInfo(nick,numOfPlayers,mode);
        ((Stage)((Node)e.getSource()).getScene().getWindow()).close();
    }
}
