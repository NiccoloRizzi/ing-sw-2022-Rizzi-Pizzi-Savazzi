package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharactersEnum;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class ViewGUI extends View {
    Stage stage;
    @FXML
    Label nickname1,nickname2, nickname3,nickname4,waiting;
    @FXML
    HBox player1,player2,player3,player4,characters;
    @FXML
    Pane ChosenAssistant1,board1,ChosenAssistant2,board2,ChosenAssistant3,board3,ChosenAssistant4,board4;
    @FXML
    Button chooseAssistantB;
    @FXML
    AnchorPane center;
    ArrayList<Pane> isles;
    ArrayList<Pane> clouds;
    ArrayList<Pane> character;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void start(){}
    @Override
    public void startGame(){

//                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameView.fxml"));
//                    loader.setController(this);
//                    try {
//                        Parent root = loader.load();
// //                       stage.setScene(new Scene(root));
//                        stage.getScene().setRoot(root);
////                        stage.setFullScreen(true);
////                        stage.show();
                        refresh();
                //}catch (IOException e){
//                        e.printStackTrace();
                //}

    }

    @Override
    public void refresh() {
        Platform.runLater(
                () -> {
                    waiting.setVisible(false);
                }
        );
        showPlayers();
        showTiles();
    }


    public void showPlayers(){
        Platform.runLater(
            () -> {
                player1.setVisible(true);
                nickname1.setText(getModelView().getNickname());
                nickname1.setVisible(true);
               // chooseAssistantB.setVisible(true);
                board1.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                if (getModelView().getPlayers()[getModelView().getMyId()].getUsedAssistants().length > 0) {
                    ChosenAssistant1.setBackground(new Background(new BackgroundImage(new Image("/images\\assistente1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    ChosenAssistant1.setVisible(true);
                } else {
                    ChosenAssistant1.setVisible(false);
                }
                player2.setVisible(true);
                nickname2.setText(getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getNickname());
                nickname2.setVisible(true);
                board2.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                if (getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                    ChosenAssistant2.setBackground(new Background(new BackgroundImage(new Image("/images\\assistente1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    ChosenAssistant2.setVisible(true);
                } else {
                    ChosenAssistant2.setVisible(false);
                }
                if (getModelView().getPlayers().length >= 3) {
                    player3.setVisible(true);
                    nickname3.setText(getModelView().getPlayers()[(getModelView().getMyId() + 2) % getModelView().getPlayers().length].getNickname());
                    board3.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    if (getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                        ChosenAssistant3.setBackground(new Background(new BackgroundImage(new Image("/images\\assistente1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        ChosenAssistant3.setVisible(true);
                    } else {
                        ChosenAssistant3.setVisible(false);
                    }
                }
                if (getModelView().getPlayers().length == 4) {
                    player4.setVisible(true);
                    nickname4.setText(getModelView().getPlayers()[(getModelView().getMyId() + 3) % getModelView().getPlayers().length].getNickname());
                    board4.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    if (getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                        ChosenAssistant4.setBackground(new Background(new BackgroundImage(new Image("/images\\assistente1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        ChosenAssistant4.setVisible(true);
                    } else {
                        ChosenAssistant4.setVisible(false);
                    }
                }
                showCharacters();
            }
        );
    }

    public void showTiles() {
        Platform.runLater(
                () -> {
                    isles = new ArrayList<>();
                    double rotation = 2*PI/(getModelView().getGameModel().getIsles().size());
                    center.getChildren().clear();
                    for(int i = 0; i < getModelView().getGameModel().getIsles().size(); i++)
                    {
                        isles.add(new Pane());
                        isles.get(i).setMaxWidth(100);
                        isles.get(i).setMaxHeight(100);
                        isles.get(i).setPrefWidth(100);
                        isles.get(i).setPrefHeight(100);
                        isles.get(i).setMinWidth(100);
                        isles.get(i).setMinHeight(100);
                        center.getChildren().add(isles.get(i));
                        isles.get(i).setLayoutX((center.getWidth()/2)+(center.getWidth()/3)*cos(rotation*i)-50);
                        isles.get(i).setLayoutY((center.getHeight()/2)+(center.getHeight()/3)*sin(rotation*i)-50);
                        isles.get(i).setBackground(new Background(new BackgroundImage(new Image("/images\\isle"+(new Random()).nextInt(3)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    }

                    clouds = new ArrayList<>();
                    rotation = 2*PI/(getModelView().getClouds().length);
                    for(int i = 0; i < getModelView().getClouds().length; i++)
                    {
                        clouds.add(i,new Pane());
                        clouds.get(i).setMaxWidth(80);
                        clouds.get(i).setMaxHeight(80);
                        clouds.get(i).setPrefWidth(80);
                        clouds.get(i).setPrefHeight(80);
                        clouds.get(i).setMinWidth(80);
                        clouds.get(i).setMinHeight(80);
                        center.getChildren().add(i,clouds.get(i));
                        clouds.get(i).setLayoutX((center.getWidth()/2)+(center.getHeight()/6)*cos(rotation*i)-40);
                        clouds.get(i).setLayoutY((center.getHeight()/2)+(center.getHeight()/6)*sin(rotation*i)-40);
                        clouds.get(i).setBackground(new Background(new BackgroundImage(new Image("/images\\cloud1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    }
                }
        );
    }

    public void showCharacters() {
        character = new ArrayList<>();
        if(getModelView().isExpert()){
            characters.setVisible(true);
            characters.getChildren().clear();
            for (int i = 0; i < 3; i++) {
                character.add(new Pane());
                characters.getChildren().add(character.get(i));
                character.get(i).setPrefHeight(150);
                character.get(i).setPrefWidth(99);
                character.get(i).setMinHeight(150);
                character.get(i).setMinWidth(99);
                character.get(i).setMaxHeight(150);
                character.get(i).setMaxWidth(99);
                character.get(i).setBackground(new Background(new BackgroundImage(new Image("/images\\personaggi\\"+getModelView().getCharacters()[i].getCard().toString()+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                if(getModelView().getCharacters()[i].getCard() == CharactersEnum.ONE_STUD_TO_TABLES)
                {
                    System.out.println("1");
                    character.get(i).getChildren().add(new GridPane());
                    ((GridPane)character.get(i).getChildren().get(0)).setMinWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setPrefWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setPrefHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setLayoutX(character.get(i).getWidth()/2 -40);
                    ((GridPane)character.get(i).getChildren().get(0)).setLayoutY(3*character.get(i).getHeight()/4 -40);
                    ((GridPane)character.get(i).getChildren().get(0)).setGridLinesVisible(true);
                    for(int j = 0;j<3;j++ )
                    ((GridPane)character.get(i).getChildren().get(0)).addColumn(j);
                }
                if(getModelView().getCharacters()[i].getCard() == CharactersEnum.EXCHANGE_3_STUD)
                {
                    System.out.println("2");
                    character.get(i).getChildren().add(new GridPane());
                    ((GridPane)character.get(i).getChildren().get(0)).setMinWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setPrefWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setPrefHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setLayoutX(character.get(i).getWidth()/2 -40);
                    ((GridPane)character.get(i).getChildren().get(0)).setLayoutY(3*character.get(i).getHeight()/4 -40);
                    ((GridPane)character.get(i).getChildren().get(0)).setGridLinesVisible(true);
                    for(int j = 0;j<3;j++ )
                        ((GridPane)character.get(i).getChildren().get(0)).addColumn(j);
                }
                if(getModelView().getCharacters()[i].getCard() == CharactersEnum.ONE_STUD_TO_ISLE)
                {
                    System.out.println("3");
                    character.get(i).getChildren().add(new GridPane());
                    ((GridPane)character.get(i).getChildren().get(0)).setMinWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setPrefWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setPrefHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinWidth(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setMinHeight(80);
                    ((GridPane)character.get(i).getChildren().get(0)).setLayoutX(character.get(i).getWidth()/2 -40);
                    ((GridPane)character.get(i).getChildren().get(0)).setLayoutY(3*character.get(i).getHeight()/4 -40);
                    ((GridPane)character.get(i).getChildren().get(0)).setGridLinesVisible(true);
                    for(int j = 0;j<3;j++ )
                        ((GridPane)character.get(i).getChildren().get(0)).addColumn(j);
                }

            }
            System.out.println("6");
        }
    }
}
