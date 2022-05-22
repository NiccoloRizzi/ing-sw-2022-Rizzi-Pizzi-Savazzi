package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewGui extends View {
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

    public void connect(ActionEvent e) throws IOException
    {
        String ip = serverIP.getText();
        System.out.println(ip);
        String port = serverPort.getText();
        System.out.println(port);

        notifyConnection(ip,Integer.parseInt(port));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/playerInfo.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void sendInfo(ActionEvent e)
    {
        String nick = nickname.getText();
        int numOfplayers = 2;
        if(players3.isSelected())
            numOfplayers = 3;
        if(players4.isSelected())
            numOfplayers = 4;
        boolean mode = expert.isSelected();
        System.out.println(nick);
        System.out.println(numOfplayers);
        System.out.println(mode);
        sendPlayerInfo(nick,numOfplayers,mode);
    }
    @Override
    public void start() {

    }

    @Override
    public void refresh() {
    }
}
