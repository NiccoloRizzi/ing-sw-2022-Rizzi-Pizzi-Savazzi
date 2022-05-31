package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.ClientBoard;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static java.lang.Math.*;

public class ViewGUI extends View {
    Stage stage;
    @FXML
    Label nickname1,nickname2, nickname3,nickname4,money1,money2,money3,money4,waiting,turn,phase;
    @FXML
    HBox player1,player2,player3,player4,characters;
    @FXML
    Pane ChosenAssistant1,board1,ChosenAssistant2,board2,ChosenAssistant3,board3,ChosenAssistant4,board4;
    @FXML
    GridPane entrance1,entrance2,entrance3,entrance4,tables1,tables2,tables3,tables4,towers1,towers2,towers3,towers4;
    @FXML
    Button chooseAssistantB;
    @FXML
    AnchorPane center;
    ArrayList<Pane> isles;
    ArrayList<Pane> clouds;
    ArrayList<Pane> character;
    ArrayList<Pane> students1,students2,students3,students4;
    Boolean started = false;
    Boolean ended = false;
    Boolean error = false;
    Popup assistant,colourPopup;
    Optional<Pane> selectedStudent;
    Optional<Integer> charIndex;
    
    int myID;

    int[] image;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void start(){}
    @Override
    public void startGame(){
        image = new Random().ints(0,3).limit(12).toArray();
        selectedStudent = Optional.empty();
        charIndex =Optional.empty();
        myID=getModelView().getMyId();
        Platform.runLater(
                () -> {
                    waiting.setVisible(false);
                }
        );
        createPlayers();
        createAssistant();
        started=true;
        refresh();
    }

    @Override
    public void refresh() {
        if(started) {
            if(getModelView().isExpert())
                refreshCharacters();
            refreshBoard();
            refreshPlayer();
            refreshTurn();
            showTiles();
            showWinner();
        }
        showErrors();
    }


    public void createPlayers(){
        Platform.runLater(
            () -> {
                player1.setVisible(true);
                nickname1.setText(getModelView().getNickname());
                nickname1.setVisible(true);
               // chooseAssistantB.setVisible(true);
                board1.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                player2.setVisible(true);
                nickname2.setText(getModelView().getPlayers()[(myID + 1) % getModelView().getPlayers().length].getNickname());
                nickname2.setVisible(true);
                board2.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                if (getModelView().getPlayers().length >= 3) {
                    player3.setVisible(true);
                    nickname3.setText(getModelView().getPlayers()[(myID + 2) % getModelView().getPlayers().length].getNickname());
                    board3.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }

                if (getModelView().getPlayers().length == 4) {
                    player4.setVisible(true);
                    nickname4.setText(getModelView().getPlayers()[(myID + 3) % getModelView().getPlayers().length].getNickname());
                    board4.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }

                if(getModelView().isExpert())
                    createCharacters();
                createBoards();
            }
        );
    }

    public void createBoards(){
        Pane pane;
        students1=new ArrayList<>();

        for(int i = 0; i< 9;i++) {
            pane = new Pane();
            pane.setPrefWidth(13);
            pane.setPrefHeight(13);
            pane.setMaxWidth(13);
            pane.setMaxHeight(13);
            students1.add(pane);
            if (i < 4) {
                entrance1.add(pane, 1, 3 + (2 * i));
            }
            else {
                entrance1.add(pane, 3, 1+2 * (i - 4));
            }
        }

        students2= new ArrayList<>();
        for(int i = 0; i< 9;i++) {
            pane = new Pane();
            pane.setPrefWidth(18);
            pane.setPrefHeight(18);
            pane.setMaxWidth(18);
            pane.setMaxHeight(18);
            students2.add(pane);
            if (i < 4)
                entrance2.add(pane, 1, 3 + 2 * i);
            else
                entrance2.add(pane, 3, 1+ 2 * (i - 4));
        }
        if(getModelView().getPlayers().length>=3) {
            students3 = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                pane = new Pane();
                pane.setPrefWidth(13);
                pane.setPrefHeight(13);
                pane.setMaxWidth(13);
                pane.setMaxHeight(13);
                students3.add(pane);
                if (i < 4)
                    entrance3.add(pane, 1, 3 + 2 * i);
                else
                    entrance3.add(pane, 3, 1+2 *( i - 4));
            }
        }
        if(getModelView().getPlayers().length==4) {
            students4 = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                pane = new Pane();
                pane.setPrefWidth(13);
                pane.setPrefHeight(13);
                pane.setMaxWidth(13);
                pane.setMaxHeight(13);
                students4.add(pane);
                if (i < 4)
                    entrance4.add(pane, 1, 3 + 2 * i);
                else
                    entrance4.add(pane, 3, 1+ 2 * (i - 4));
            }
        }
    }
    public void showTiles() {
        Platform.runLater(
                () -> {
                    isles = new ArrayList<>();
                    double rotation = 2*PI/(getModelView().getGameModel().getIsles().size());
                    center.getChildren().clear();
                    AnchorPane tile;
                    StackPane student;
                    Label num;
                    for(int i = 0; i < getModelView().getGameModel().getIsles().size(); i++)
                    {
                        tile = new AnchorPane();
                        tile.setMaxWidth(100);
                        tile.setMaxHeight(100);
                        tile.setPrefWidth(100);
                        tile.setPrefHeight(100);
                        tile.setMinWidth(100);
                        tile.setMinHeight(100);
                        tile.setBackground(new Background(new BackgroundImage(new Image("/images\\isle"+image[i]+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        tile.setOnMouseClicked(mouseEvent -> {selectIsle(mouseEvent);});
                        isles.add(tile);
                        center.getChildren().add(isles.get(i));
                        isles.get(i).setLayoutX((center.getWidth()/2)+(center.getWidth()/3)*cos(rotation*i)-50);
                        isles.get(i).setLayoutY((center.getHeight()/2)+(center.getHeight()/3)*sin(rotation*i)-50);
                        for(Colour c:Colour.values())
                        {
                            student = new StackPane();
                            student.setMinHeight(20);
                            student.setMinWidth(20);
                            student.setPrefHeight(20);
                            student.setPrefWidth(20);
                            student.setMaxHeight(20);
                            student.setMaxWidth(20);
                            student.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_"+ c.toString()+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            student.setAlignment(Pos.CENTER);
                            num = new Label(getModelView().getGameModel().getIsles().get(i).getStudents().get(c).toString());
                            num.setMaxHeight(20);
                            num.setMaxWidth(20);
                            num.setAlignment(Pos.CENTER);
                            num.setTextAlignment(TextAlignment.CENTER);
                            num.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12.0));
                            student.getChildren().add(num);
                            tile.getChildren().add(student);
                            tile.setTopAnchor(student,50+30*cos(c.ordinal()*2*PI/5)-10);
                            tile.setRightAnchor(student,50+30*sin(c.ordinal()*2*PI/5)-10);
                        }
                        if(getModelView().getGameModel().getIsles().get(i).getControlling()!= Faction.Empty)
                        {
                            student = new StackPane();
                            student.setAlignment(Pos.CENTER);
                            student.setMinHeight(40);
                            student.setMinWidth(40);
                            student.setPrefHeight(40);
                            student.setPrefWidth(40);
                            student.setMaxHeight(40);
                            student.setMaxWidth(40);
                            student.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\tower_"+getModelView().getGameModel().getIsles().get(i).getControlling() +".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            num = new Label(((Integer)getModelView().getGameModel().getIsles().get(i).getSize()).toString());
                            num.setMaxHeight(36);
                            num.setMaxWidth(36);
                            num.setPrefHeight(36);
                            num.setPrefWidth(36);
                            num.setMinHeight(36);
                            num.setMinWidth(36);
                            num.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12.0));
                            num.setTextFill((getModelView().getGameModel().getIsles().get(i).getControlling() == Faction.Black)? Paint.valueOf("white"):Paint.valueOf("black"));
                            student.getChildren().add(num);
                            num.setAlignment(Pos.CENTER);
                            num.setTextAlignment(TextAlignment.CENTER);
                            tile.getChildren().add(student);
                            tile.setTopAnchor(student,50.0-18);
                            tile.setRightAnchor(student,50.0-18);
                        }
                    }
                    student = new StackPane();
                    student.setMinHeight(20);
                    student.setMinWidth(20);
                    student.setPrefHeight(20);
                    student.setPrefWidth(20);
                    student.setMaxHeight(20);
                    student.setMaxWidth(20);
                    student.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\MotherNature.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    isles.get(getModelView().getGameModel().getMotherNature()).getChildren().add(student);
                    ((AnchorPane)isles.get(getModelView().getGameModel().getMotherNature())).setTopAnchor(student,15.0);
                    ((AnchorPane)isles.get(getModelView().getGameModel().getMotherNature())).setLeftAnchor(student,50.0-11);


                    clouds = new ArrayList<>();
                    rotation = 2*PI/(getModelView().getClouds().length);
                    for(int i = 0; i < getModelView().getClouds().length; i++)
                    {
                        tile = new AnchorPane();
                        clouds.add(i,tile);
                        clouds.get(i).setMaxWidth(80);
                        clouds.get(i).setMaxHeight(80);
                        clouds.get(i).setPrefWidth(80);
                        clouds.get(i).setPrefHeight(80);
                        clouds.get(i).setMinWidth(80);
                        clouds.get(i).setMinHeight(80);
                        clouds.get(i).setOnMouseClicked(mouseEvent -> {selectCloud(mouseEvent);});
                        center.getChildren().add(i,clouds.get(i));
                        clouds.get(i).setLayoutX((center.getWidth()/2)+(center.getHeight()/6)*cos(rotation*i)-40);
                        clouds.get(i).setLayoutY((center.getHeight()/2)+(center.getHeight()/6)*sin(rotation*i)-40);
                        clouds.get(i).setBackground(new Background(new BackgroundImage(new Image("/images\\cloud1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        for(Colour c:Colour.values())
                        {
                            student = new StackPane();
                            student.setMinHeight(20);
                            student.setMinWidth(20);
                            student.setPrefHeight(20);
                            student.setPrefWidth(20);
                            student.setMaxHeight(20);
                            student.setMaxWidth(20);
                            student.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_"+ c.toString()+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            student.setAlignment(Pos.CENTER);
                            num = new Label(getModelView().getClouds()[i].getStudents().get(c).toString());
                            num.setMaxHeight(20);
                            num.setMaxWidth(20);
                            num.setAlignment(Pos.CENTER);
                            num.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12.0));
                            num.setTextAlignment(TextAlignment.CENTER);
                            student.getChildren().add(num);
                            tile.getChildren().add(student);
                            tile.setTopAnchor(student,40+25*cos(c.ordinal()*2*PI/5)-10);
                            tile.setRightAnchor(student,40+25*sin(c.ordinal()*2*PI/5)-10);
                        }
                    }
                }
        );
    }

    public void createCharacters() {
        character = new ArrayList<>();
        if(getModelView().isExpert()){
            characters.setVisible(true);
            characters.getChildren().clear();
            for (int i = 0; i < 3; i++) {
                character.add(new StackPane());
                characters.getChildren().add(character.get(i));
                character.get(i).setPrefHeight(150);
                character.get(i).setPrefWidth(99);
                character.get(i).setMinHeight(150);
                character.get(i).setMinWidth(99);
                character.get(i).setMaxHeight(150);
                character.get(i).setMaxWidth(99);
                character.get(i).setOnMouseClicked(mouseEvent -> selectCharacter(mouseEvent));
                ((StackPane) character.get(i)).setAlignment(Pos.CENTER);
                character.get(i).setBackground(new Background(new BackgroundImage(new Image("/images\\personaggi\\" + getModelView().getCharacters()[i].getCard().toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                character.get(i).getChildren().add(new GridPane());
                ((GridPane) character.get(i).getChildren().get(0)).setAlignment(Pos.BOTTOM_CENTER);
                ((GridPane) character.get(i).getChildren().get(0)).setMinWidth(character.get(i).getWidth());
                ((GridPane) character.get(i).getChildren().get(0)).setMinHeight(character.get(i).getHeight() / 2);
                ((GridPane) character.get(i).getChildren().get(0)).setPrefWidth(character.get(i).getWidth());
                ((GridPane) character.get(i).getChildren().get(0)).setPrefHeight(character.get(i).getHeight() / 2);
                ((GridPane) character.get(i).getChildren().get(0)).setMinWidth(character.get(i).getWidth());
                ((GridPane) character.get(i).getChildren().get(0)).setMinHeight(character.get(i).getHeight() / 2);
                ((GridPane) character.get(i).getChildren().get(0)).setHgap(20);
                ((GridPane) character.get(i).getChildren().get(0)).setVgap(30);
                //((GridPane) character.get(i).getChildren().get(0)).setGridLinesVisible(true);
                for (int j = 0; j < 3; j++) {
                    ((GridPane) character.get(i).getChildren().get(0)).addColumn(j);
                    ((GridPane) character.get(i).getChildren().get(0)).addRow(j);
                }
            }
        }
    }

    public void refreshTurn()
    {
        Platform.runLater(
            ()-> {
                turn.setText("Turn of " + getModelView().getPlayers()[getModelView().getTurn().getPlayerId()].getNickname());
                phase.setText("Phase: " + getModelView().getTurn().getTurn().getTurnMsg());
                turn.setVisible(true);
                phase.setVisible(true);
            }
        );
    }

    public void refreshCharacters() {
        Platform.runLater(
            ()-> {
                for (int i = 0; i < 3; i++) {
                    if(getModelView().getCharacters()[i].getPrice()>getModelView().getCharacters()[i].getCard().getPrice()&&characters.getChildren().size()<2)
                    {
                        Pane pane = new Pane();
                        pane.setMaxWidth(20);
                        pane.setMaxHeight(20);
                        pane.setPrefWidth(20);
                        pane.setPrefHeight(20);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\Moneta_base.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        character.get(i).getChildren().add(pane);
                        ((StackPane)character.get(i)).setAlignment(pane,Pos.TOP_CENTER);
                    }
                    if (getModelView().getCharacters()[i].getCard() == CharactersEnum.ONE_STUD_TO_TABLES || (getModelView().getCharacters()[i].getCard() == CharactersEnum.EXCHANGE_3_STUD) || getModelView().getCharacters()[i].getCard() == CharactersEnum.ONE_STUD_TO_ISLE) {
                        Pane pane;
                        int column = 0;
                        int row = 0;
                        ((GridPane) character.get(i).getChildren().get(0)).getChildren().clear();
                        for (Colour c : Colour.values()) {
                            for (int j = 0; j < getModelView().getCharacters()[i].getStudents().get(c); j++) {
                                pane = new Pane();
                                pane.setMaxWidth(20);
                                pane.setMaxHeight(20);
                                pane.setPrefWidth(20);
                                pane.setPrefHeight(20);
                                pane.setUserData(c);
                                pane.setOnMouseClicked(mouseEvent -> selectCharacterStudent(mouseEvent));
                                pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                                ((GridPane) character.get(i).getChildren().get(0)).add(pane, column, row);
                                if (column == 2) {
                                    column = 0;
                                    row++;
                                } else {
                                    column++;
                                }

                            }
                        }

                    }
                    if (getModelView().getCharacters()[i].getCard() == CharactersEnum.PROHIBITED) {
                        Pane pane;
                        int column = 0;
                        int row = 0;
                        ((GridPane) character.get(i).getChildren().get(0)).getChildren().clear();
                        for (int j = 0; j < getModelView().getGameModel().getProhibited(); j++) {
                            pane = new Pane();
                            pane.setMaxWidth(20);
                            pane.setMaxHeight(20);
                            pane.setPrefWidth(20);
                            pane.setPrefHeight(20);
                            pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\prohibited.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            ((GridPane) character.get(i).getChildren().get(0)).add(pane, column, row);
                            if (column == 2) {
                                column = 0;
                                row++;
                            } else {
                                column++;
                            }
                        }
                    }
                }
            }
        );
    }

    public void refreshBoard(){
        Platform.runLater(
            () -> {
                ClientBoard board =getModelView().getBoards()[0];
                Pane pane;
                int j = 0;
                for(ClientBoard b: getModelView().getBoards())
                {
                    if(b.getPlayerID() == myID)
                    {
                        board = b;
                    }
                }
                for(Pane p: students1)
                {
                    p.setBackground(null);
                    p.setOnMouseClicked(null);
                }
                for(Colour c: Colour.values())
                {
                    for(int i = 0; i < board.getEntrance().get(c);i++){
                        students1.get(j).setUserData(c);
                        students1.get(j).setOnMouseClicked(mouseEvent-> {selectEntranceStudent(mouseEvent);});
                        students1.get(j).setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_"+ c.toString()+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        j++;
                    }
                 }
                tables1.getChildren().clear();
                for(Colour c: Colour.values()) {
                    for (int i = 0; i < board.getTables().get(c); i++) {
                        pane = new Pane();
                        pane.setMinHeight(13);
                        pane.setMinWidth(13);
                        pane.setPrefHeight(13);
                        pane.setPrefWidth(13);
                        pane.setMaxHeight(13);
                        pane.setMaxWidth(13);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                        tables1.add(pane, 1 + 2 * i, 1 + 2 * c.ordinal());
                    }
                    if (getModelView().getGameModel().getProfessors().containsKey(c)&&getModelView().getGameModel().getProfessors().get(c) == myID) {
                        pane = new Pane();
                        pane.setMinHeight(13);
                        pane.setMinWidth(13);
                        pane.setPrefHeight(13);
                        pane.setPrefWidth(13);
                        pane.setMaxHeight(13);
                        pane.setMaxWidth(13);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\professor_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        tables1.add(pane, 21, 1 + 2 * c.ordinal());
                    }
                }

                towers1.getChildren().clear();
                for(int i =0; i<board.getTowers();i++)
                {
                    pane = new Pane();
                    pane.setMinHeight(20);
                    pane.setMinWidth(20);
                    pane.setPrefHeight(20);
                    pane.setPrefWidth(20);
                    pane.setMaxHeight(20);
                    pane.setMaxWidth(20);
                    pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\tower_" + board.getFaction() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                    if(i<4)
                        towers1.add(pane,1,i+1);
                    else
                        towers1.add(pane,2,i-3);
                }

                j = 0;
                for (ClientBoard b : getModelView().getBoards()) {
                    if (b.getPlayerID() == (myID + 1) % getModelView().getPlayers().length) {
                        board = b;
                    }
                }
                for (Colour c : Colour.values()) {
                    for (int i = 0; i < board.getEntrance().get(c); i++) {
                        students2.get(j).setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        j++;
                    }
                }
                tables2.getChildren().clear();
                for (Colour c : Colour.values()) {
                    for (int i = 0; i < board.getTables().get(c); i++) {
                        pane = new Pane();
                        pane.setMinHeight(13);
                        pane.setMinWidth(13);
                        pane.setPrefHeight(13);
                        pane.setPrefWidth(13);
                        pane.setMaxHeight(13);
                        pane.setMaxWidth(13);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                        tables2.add(pane, 1 + 2 * i, 1 + 2 * c.ordinal());
                    }
                    if (getModelView().getGameModel().getProfessors().containsKey(c) && getModelView().getGameModel().getProfessors().get(c) == (myID + 1) % getModelView().getPlayers().length) {
                        pane = new Pane();
                        pane.setMinHeight(13);
                        pane.setMinWidth(13);
                        pane.setPrefHeight(13);
                        pane.setPrefWidth(13);
                        pane.setMaxHeight(13);
                        pane.setMaxWidth(13);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\professor_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        tables2.add(pane, 21, 1 + 2 * c.ordinal());
                    }
                }

                towers2.getChildren().clear();
                for(int i =0; i<board.getTowers();i++)
                {

                    pane = new Pane();
                    pane.setMinHeight(20);
                    pane.setMinWidth(20);
                    pane.setPrefHeight(20);
                    pane.setPrefWidth(20);
                    pane.setMaxHeight(20);
                    pane.setMaxWidth(20);
                    pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\tower_" + board.getFaction() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                    if(i<4)
                        towers2.add(pane,1,i+1);
                    else
                        towers2.add(pane,2,i-3);
                }

                if (getModelView().getPlayers().length >= 3) {
                    j = 0;
                    for (ClientBoard b : getModelView().getBoards()) {
                        if (b.getPlayerID() == (myID + 1) % getModelView().getPlayers().length) {
                            board = b;
                        }
                    }
                    for (Colour c : Colour.values()) {
                        for (int i = 0; i < board.getEntrance().get(c); i++) {
                            students3.get(j).setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            j++;
                        }
                    }
                    tables3.getChildren().clear();
                    for (Colour c : Colour.values()) {
                        pane = new Pane();
                        pane.setMinHeight(13);
                        pane.setMinWidth(13);
                        pane.setPrefHeight(13);
                        pane.setPrefWidth(13);
                        pane.setMaxHeight(13);
                        pane.setMaxWidth(13);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        for (int i = 0; i < board.getTables().get(c); i++) {
                            pane = new Pane();
                            pane.setMinHeight(13);
                            pane.setMinWidth(13);
                            pane.setPrefHeight(13);
                            pane.setPrefWidth(13);
                            pane.setMaxHeight(13);
                            pane.setMaxWidth(13);
                            pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                            tables3.add(pane, 1 + 2 * i, 1 + 2 * c.ordinal());
                        }
                        if (getModelView().getGameModel().getProfessors().containsKey(c) && getModelView().getGameModel().getProfessors().get(c) == (myID + 2) % getModelView().getPlayers().length) {
                            pane = new Pane();
                            pane.setMinHeight(13);
                            pane.setMinWidth(13);
                            pane.setPrefHeight(13);
                            pane.setPrefWidth(13);
                            pane.setMaxHeight(13);
                            pane.setMaxWidth(13);
                            pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\professor_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            tables3.add(pane, 21, 1 + 2 * c.ordinal());
                        }
                    }

                    towers3.getChildren().clear();
                    for(int i =0; i<board.getTowers();i++)
                    {
                        pane = new Pane();
                        pane.setMinHeight(20);
                        pane.setMinWidth(20);
                        pane.setPrefHeight(20);
                        pane.setPrefWidth(20);
                        pane.setMaxHeight(20);
                        pane.setMaxWidth(20);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\tower_" + board.getFaction() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                        if(i<4)
                            towers3.add(pane,1,i+1);
                        else
                            towers3.add(pane,2,i-3);
                    }
                }
                if (getModelView().getPlayers().length == 4) {
                    j = 0;
                    for (ClientBoard b : getModelView().getBoards()) {
                        if (b.getPlayerID() == (myID + 1) % getModelView().getPlayers().length) {
                            board = b;
                        }
                    }
                    for (Colour c : Colour.values()) {
                        for (int i = 0; i < board.getEntrance().get(c); i++) {
                            students4.get(j).setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            j++;
                        }
                    }
                    tables4.getChildren().clear();
                    for (Colour c : Colour.values()) {
                        for (int i = 0; i < board.getTables().get(c); i++) {
                            pane = new Pane();
                            pane.setMinHeight(13);
                            pane.setMinWidth(13);
                            pane.setPrefHeight(13);
                            pane.setPrefWidth(13);
                            pane.setMaxHeight(13);
                            pane.setMaxWidth(13);
                            pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                            tables4.add(pane, 1 + 2 * i, 1 + 2 * c.ordinal());
                        }
                        if ( getModelView().getGameModel().getProfessors().containsKey(c) && getModelView().getGameModel().getProfessors().get(c) == (myID + 3) % getModelView().getPlayers().length) {
                            pane = new Pane();
                            pane.setMinHeight(13);
                            pane.setMinWidth(13);
                            pane.setPrefHeight(13);
                            pane.setPrefWidth(13);
                            pane.setMaxHeight(13);
                            pane.setMaxWidth(13);
                            pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\professor_" + c.toString() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            tables4.add(pane, 21, 1 + 2 * c.ordinal());
                        }
                    }

                    towers4.getChildren().clear();
                    for(int i =0; i<board.getTowers();i++)
                    {
                        pane = new Pane();
                        pane.setMinHeight(20);
                        pane.setMinWidth(20);
                        pane.setPrefHeight(20);
                        pane.setPrefWidth(20);
                        pane.setMaxHeight(20);
                        pane.setMaxWidth(20);
                        pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\tower_" + board.getFaction() + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                        if(i<4)
                            towers4.add(pane,1,i+1);
                        else
                            towers4.add(pane,2,i-3);
                    }
                }
            }
        );
    }

    public void refreshPlayer() {
        Platform.runLater(
            ()-> {
                money1.setText(((Integer) getModelView().getPlayers()[myID].getCoins()).toString());
                if (getModelView().getPlayers()[myID].getUsedAssistants().length > 0) {
                    ChosenAssistant1.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente"+(getModelView().getPlayers()[myID].getUsedAssistants()[getModelView().getPlayers()[myID].getUsedAssistants().length-1]+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                } else {
                    ChosenAssistant1.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago"+(myID+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }

                money2.setText(((Integer) getModelView().getPlayers()[(myID + 1) % getModelView().getPlayers().length].getCoins()).toString());
                if (getModelView().getPlayers()[(myID + 1) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                    ChosenAssistant2.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente"+(getModelView().getPlayers()[(myID + 1) % getModelView().getPlayers().length].getUsedAssistants()[getModelView().getPlayers()[(myID + 1) % getModelView().getPlayers().length].getUsedAssistants().length-1]+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                } else {
                    ChosenAssistant2.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago"+((myID + 1)%getModelView().getPlayers().length+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }


                if (getModelView().getPlayers().length >= 3) {
                    money3.setText(((Integer) getModelView().getPlayers()[(myID + 2) % getModelView().getPlayers().length].getCoins()).toString());
                    if (getModelView().getPlayers()[(myID + 2) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                        ChosenAssistant3.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente" + (getModelView().getPlayers()[(myID + 2) % getModelView().getPlayers().length].getUsedAssistants()[getModelView().getPlayers()[(myID + 2) % getModelView().getPlayers().length].getUsedAssistants().length - 1]+1)+ ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    } else {
                        ChosenAssistant3.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago" + ((myID + 2) % getModelView().getPlayers().length+1) + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    }
                }

                if (getModelView().getPlayers().length == 4) {
                    money4.setText(((Integer) getModelView().getPlayers()[(myID + 3) % getModelView().getPlayers().length].getCoins()).toString());
                    if (getModelView().getPlayers()[(myID + 3) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                        ChosenAssistant4.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente"+(getModelView().getPlayers()[(myID + 3) % getModelView().getPlayers().length].getUsedAssistants()[getModelView().getPlayers()[(myID + 3) % getModelView().getPlayers().length].getUsedAssistants().length-1]+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    } else {
                        ChosenAssistant4.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago"+((myID + 3) % getModelView().getPlayers().length+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    }
                }
            }
        );
    }

    public void createAssistant()
    {
        Platform.runLater(
            ()-> {
                assistant = new Popup();
                HBox box = new HBox();
                box.setVisible(true);
                box.setSpacing(10);
                assistant.getContent().add(box);
                assistant.setHeight(200);
                assistant.setWidth(1000);
                box.setAlignment(Pos.CENTER);
//                assistant.setAnchorX(stage.getScene().getWidth()/ 2);
//                assistant.setAnchorY(stage.getScene().getHeight()/ 2);
                assistant.hide();
            }
        );
    }

    public void showAssistant(ActionEvent e)
    {
        HBox box = (HBox) assistant.getContent().get(0);
        Pane pane;
        box.getChildren().clear();
        for(int i = 0; i<getModelView().getPlayers()[myID].getDeck().length;i++)
        {
            pane = new Pane();
            pane.setMaxHeight(200);
            pane.setMaxWidth(100);
            pane.setPrefHeight(200);
            pane.setMinHeight(150);
            pane.setMinWidth(137);
            pane.setVisible(true);
            pane.setOnMouseClicked(mouseEvent ->{chooseAssistant(mouseEvent);});
            pane.setUserData(getModelView().getPlayers()[myID].getDeck()[i]);
            pane.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente"+(getModelView().getPlayers()[myID].getDeck()[i]+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
            box.getChildren().add(pane);
        }
        assistant.sizeToScene();
        assistant.setX(stage.getScene().getWidth()/2 -(137+10)*getModelView().getPlayers()[myID].getDeck().length/2);
        assistant.setY(stage.getScene().getHeight()/2 - assistant.getHeight());
        assistant.show(stage);
    }
    public void chooseAssistant(MouseEvent e){
        int chosen = (int)((Pane)e.getSource()).getUserData();
        System.out.println(chosen);
        ChooseAssistant(chosen+1);
        error= false;
        assistant.hide();
    }

    public void selectEntranceStudent(MouseEvent e){
        int sum = 0;
        System.out.println("student");
        selectedStudent = Optional.of((Pane)e.getSource());
    }

    public void selectCharacterStudent(MouseEvent e)
    {
        System.out.println("selected student");
        System.out.println(charIndex.get());
        System.out.println(character.indexOf((((Pane)e.getSource()).getParent()).getParent()));
        if(charIndex.isPresent() && charIndex.get()==character.indexOf((((Pane)e.getSource()).getParent()).getParent()))
        {
            if(getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.ONE_STUD_TO_TABLES || getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.ONE_STUD_TO_ISLE)
                selectedStudent = Optional.of(((Pane)e.getSource()));
        }
    }
    public void selectIsle(MouseEvent e)
    {
        System.out.println("isle");
        if(selectedStudent.isPresent()){
            if(charIndex.isPresent()&&getModelView().getCharacters()[charIndex.get()].getCard()==CharactersEnum.ONE_STUD_TO_ISLE)
            {
                charStudToIsle(charIndex.get(),((Colour)selectedStudent.get().getUserData()), isles.indexOf((Pane) e.getSource()));
                selectedStudent.get().setBackground(null);
                selectedStudent.get().setOnMouseClicked(null);
                selectedStudent = Optional.empty();
                charIndex = Optional.empty();
                error= false;
            }else {
                MoveToIsle(((Colour) selectedStudent.get().getUserData()), isles.indexOf((Pane) e.getSource()));
                selectedStudent.get().setBackground(null);
                selectedStudent.get().setOnMouseClicked(null);
                selectedStudent = Optional.empty();
                error = false;
            }
        }
        else if(getModelView().getTurn().getTurn() == TurnMessage.Turn.ACTION_MN&&charIndex.isEmpty())
        {
            int moves = isles.indexOf((Pane)e.getSource())-getModelView().getGameModel().getMotherNature();
            System.out.println(moves);
            System.out.println((moves<0)?getModelView().getGameModel().getIsles().size()+moves:moves);
            MoveMotherNature((moves<0)?getModelView().getGameModel().getIsles().size()+moves:moves);
            error= false;
        }
    }

    public void selectTable(MouseEvent e)
    {
        System.out.println("table");
        if(!selectedStudent.isEmpty()){
            if(charIndex.isPresent()&&getModelView().getCharacters()[charIndex.get()].getCard()==CharactersEnum.ONE_STUD_TO_TABLES)
            {
                charStudToTable(charIndex.get(),((Colour)selectedStudent.get().getUserData()));
                selectedStudent.get().setBackground(null);
                selectedStudent.get().setOnMouseClicked(null);
                selectedStudent = Optional.empty();
                charIndex = Optional.empty();
                error= false;
            }
            else {
                MoveToTable(((Colour) selectedStudent.get().getUserData()));
                selectedStudent.get().setBackground(null);
                selectedStudent.get().setOnMouseClicked(null);
                selectedStudent = Optional.empty();
                error = false;
            }
        }
    }
    public void selectCloud(MouseEvent e)
    {
        ChooseCloud(clouds.indexOf((Pane)e.getSource()));
        error= false;
    }

    public void showErrors ()
    {
        Platform.runLater(
            ()-> {
                if (getModelView().getError() != null&&!error) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(getModelView().getError().toString());
                    alert.setContentText(getModelView().getError().getErrorMsg());
                    alert.showAndWait();
                    if(getModelView().getError() == ErrorMessage.ErrorType.PlayerDisconnected)
                    {
                        Event.fireEvent(stage,new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
                    }
                    if(getModelView().getError() == ErrorMessage.ErrorType.NicknameTaken)
                    {
                        try {
                            restartConnection();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                    error = true;
                }
            }
        );
    }

    public void selectCharacter(MouseEvent e)
    {
        if(getModelView().getCurrentCharacter().isEmpty()) {
            charIndex = Optional.of(character.indexOf((Pane) e.getSource()));
            System.out.println("character: "+charIndex.get());
            if (getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.PLUS_2_INFLUENCE || getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.NO_TOWER_INFLUENCE) {
                useInfluenceCharacter(charIndex.get());
                System.out.println(getModelView().getCharacters()[charIndex.get()].getCard());
                charIndex = Optional.empty();
            }
            else if (getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.PLUS_2_MN) {
                motherNBoost(charIndex.get());
                charIndex = Optional.empty();
            }
            else if (getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.REMOVE_3_STUD || getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.NO_COLOUR_INFLUENCE)
            {
                showColourChoice();
            }
        }
    }

    public void showColourChoice()
    {
        Platform.runLater(
                ()->{
                        colourPopup = new Popup();
                        VBox box = new VBox();
                        box.setPrefHeight(300);
                        box.setPrefWidth(600);
                        box.setSpacing(20);
                        colourPopup.getContent().add(box);
                        Label instruction = new Label("Select the colour to remove");
                        instruction.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,40.0));
                        box.getChildren().add(instruction);
                        box.setAlignment(Pos.CENTER);
                        HBox colors = new HBox();
                        colors.setSpacing(20);
                        box.getChildren().add(colors);
                        Pane pane;
                        for(Colour c: Colour.values())
                        {
                            pane = new Pane();
                            pane.setMinWidth(100);
                            pane.setMinHeight(100);
                            pane.setMaxWidth(100);
                            pane.setMaxHeight(100);
                            pane.setPrefWidth(100);
                            pane.setPrefHeight(100);
                            pane.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_"+c +".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            pane.setUserData(c);
                            pane.setOnMouseClicked(mouseEvent -> selectColour(mouseEvent));
                            colors.getChildren().add(pane);
                        }
                    colourPopup.setX(stage.getScene().getWidth()/2 -(120*5)/2);
                    colourPopup.setY(stage.getScene().getHeight()/2-300);
                    colourPopup.show(stage);
                }
        );
    }

    public void selectColour(MouseEvent e)
    {
        System.out.println((Colour) ((Pane) e.getSource()).getUserData());
        if(charIndex.isPresent()) {
            if(getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.REMOVE_3_STUD) {
                remove3Stud(charIndex.get(), (Colour) ((Pane) e.getSource()).getUserData());
                System.out.println(1);
            }
            if(getModelView().getCharacters()[charIndex.get()].getCard() == CharactersEnum.NO_COLOUR_INFLUENCE) {
                noColourInfluence(charIndex.get(), (Colour) ((Pane) e.getSource()).getUserData());
                System.out.println(2);
            }
            colourPopup.hide();
            charIndex = Optional.empty();
        }
    }

    public void restartConnection() throws IOException
    {
        FXMLLoader loader2 = new FXMLLoader();
        Stage connectionStage = new Stage();
        loader2.setLocation(getClass().getResource("/serverConnection.fxml"));
        Scene scene = new Scene(loader2.load());
        ((VBox)scene.getRoot()).setBackground(new Background(new BackgroundImage(new Image("/images\\background1.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
        connectionStage.setScene(scene);
        connectionStage.setTitle("connection to server");
        connectionStage.getIcons().add(new Image(getClass().getResourceAsStream("/images\\logo.png")));
        connectionStage.alwaysOnTopProperty();
        ((ConnectionController)loader2.getController()).setView(this);
        connectionStage.initModality(Modality.APPLICATION_MODAL);
        connectionStage.setOnCloseRequest(event-> {event.consume();connectionStage.close();});
        connectionStage.show();
    }
    public void showWinner() {
        Platform.runLater(
            () -> {
                if (getModelView().getWin() != null&&!ended) {
                    Stage winStage = new Stage();
                    StackPane pane = new StackPane();
                    pane.setPrefHeight(200);
                    pane.setPrefWidth(400);
                    Label winner = new Label();
                    if (getModelView().getWin().isDraw()) {
                        winner.setText("Draw");
                    } else {
                        if (getModelView().getPlayers().length < 4) {
                            winner.setText(getModelView().getPlayers()[getModelView().getWin().getId()].getNickname() + " has won!");
                        } else {
                            winner.setText(getModelView().getPlayers()[getModelView().getWin().getId()].getNickname() + " and " + getModelView().getPlayers()[(getModelView().getWin().getId() + 2) % 4].getNickname() + " have won!");
                        }
                    }
                    pane.getChildren().add(winner);
                    pane.setAlignment(winner,Pos.CENTER);
                    Scene scene = new Scene(pane);
                    winStage.setScene(scene);
                    winStage.show();
                    ended = true;
                    winStage.setOnCloseRequest(windowEvent -> {
                        windowEvent.consume();
                        Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
                        winStage.close();
                    });
                }
            }
        );
    }
}
