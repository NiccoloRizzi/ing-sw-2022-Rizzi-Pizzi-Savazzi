package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientBoard;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.*;

public class ViewGUI extends View {
    Stage stage;
    @FXML
    Label nickname1,nickname2, nickname3,nickname4,money1,money2,money3,money4,waiting;
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

    int[] image;
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @Override
    public void start(){}
    @Override
    public void startGame(){
        image = new Random().ints(0,3).limit(12).toArray();
        Platform.runLater(
                () -> {
                    waiting.setVisible(false);
                }
        );
        createPlayers();
        started=true;
        refresh();
    }

    @Override
    public void refresh() {
        if(started) {
            refreshCharacters();
            refreshBoard();
            refreshPlayer();
            showTiles();
        }
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
                nickname2.setText(getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getNickname());
                nickname2.setVisible(true);
                board2.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));

                if (getModelView().getPlayers().length >= 3) {
                    player3.setVisible(true);
                    nickname3.setText(getModelView().getPlayers()[(getModelView().getMyId() + 2) % getModelView().getPlayers().length].getNickname());
                    board3.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }

                if (getModelView().getPlayers().length == 4) {
                    player4.setVisible(true);
                    nickname4.setText(getModelView().getPlayers()[(getModelView().getMyId() + 3) % getModelView().getPlayers().length].getNickname());
                    board4.setBackground(new Background(new BackgroundImage(new Image("/images\\board.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }

                createCharacters();
                createBoards();
            }
        );
    }

    public void createBoards(){
        Pane pane;
        students1=new ArrayList<>();
        System.out.println(entrance1.getRowCount());
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
                            student.setMinHeight(20);
                            student.setMinWidth(20);
                            student.setPrefHeight(20);
                            student.setPrefWidth(20);
                            student.setMaxHeight(20);
                            student.setMaxWidth(20);
                            student.setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\tower_"+getModelView().getGameModel().getIsles().get(i).getControlling() +".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                            num = new Label(((Integer)getModelView().getGameModel().getIsles().get(i).getSize()).toString());
                            num.setMaxHeight(20);
                            num.setMaxWidth(20);
                            num.setPrefHeight(20);
                            num.setPrefWidth(20);
                            num.setMinHeight(20);
                            num.setMinWidth(20);
                            num.setFont(Font.font("System", FontWeight.BOLD, FontPosture.REGULAR,12.0));
                            student.getChildren().add(num);
                            num.setAlignment(Pos.CENTER);
                            num.setTextAlignment(TextAlignment.CENTER);
                            tile.getChildren().add(student);
                            tile.setTopAnchor(student,50.0-10);
                            tile.setRightAnchor(student,50.0-10);
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
                ((GridPane) character.get(i).getChildren().get(0)).setGridLinesVisible(true);
                for (int j = 0; j < 3; j++) {
                    ((GridPane) character.get(i).getChildren().get(0)).addColumn(j);
                    ((GridPane) character.get(i).getChildren().get(0)).addRow(j);
                }
            }
        }
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
                    if(b.getPlayerID() == getModelView().getMyId())
                    {
                        board = b;
                    }
                }
                for(Colour c: Colour.values())
                {
                    for(int i = 0; i < board.getEntrance().get(c);i++){
                        students1.get(j).setBackground(new Background(new BackgroundImage(new Image("/images\\pedine\\student_"+ c.toString()+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                        j++;
                    }
                    System.out.println(entrance1.getRowCount());
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
                    if (getModelView().getGameModel().getProfessors().containsKey(c)&&getModelView().getGameModel().getProfessors().get(c) == getModelView().getMyId()) {
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

                tables1.getChildren().clear();
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
                    if (b.getPlayerID() == (getModelView().getMyId() + 1) % getModelView().getPlayers().length) {
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
                    if (getModelView().getGameModel().getProfessors().containsKey(c) && getModelView().getGameModel().getProfessors().get(c) == (getModelView().getMyId() + 1) % getModelView().getPlayers().length) {
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

                tables2.getChildren().clear();
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
                        if (b.getPlayerID() == (getModelView().getMyId() + 1) % getModelView().getPlayers().length) {
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
                        if (getModelView().getGameModel().getProfessors().containsKey(c) && getModelView().getGameModel().getProfessors().get(c) == (getModelView().getMyId() + 2) % getModelView().getPlayers().length) {
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

                    tables3.getChildren().clear();
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
                        if (b.getPlayerID() == (getModelView().getMyId() + 1) % getModelView().getPlayers().length) {
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
                        if ( getModelView().getGameModel().getProfessors().containsKey(c) && getModelView().getGameModel().getProfessors().get(c) == (getModelView().getMyId() + 3) % getModelView().getPlayers().length) {
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

                    tables4.getChildren().clear();
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
                money1.setText(((Integer) getModelView().getPlayers()[getModelView().getMyId()].getCoins()).toString());
                if (getModelView().getPlayers()[getModelView().getMyId()].getUsedAssistants().length > 0) {
                    ChosenAssistant1.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente"+getModelView().getPlayers()[getModelView().getMyId()].getUsedAssistants()[getModelView().getPlayers()[getModelView().getMyId()].getUsedAssistants().length-1]+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                } else {
                    ChosenAssistant1.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago"+(getModelView().getMyId()+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }

                money2.setText(((Integer) getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getCoins()).toString());
                if (getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                    ChosenAssistant2.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente"+getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getUsedAssistants()[getModelView().getPlayers()[(getModelView().getMyId() + 1) % getModelView().getPlayers().length].getUsedAssistants().length-1]+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                } else {
                    ChosenAssistant2.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago"+((getModelView().getMyId() + 1)%getModelView().getPlayers().length+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                }


                if (getModelView().getPlayers().length >= 3) {
                    money3.setText(((Integer) getModelView().getPlayers()[(getModelView().getMyId() + 2) % getModelView().getPlayers().length].getCoins()).toString());
                    if (getModelView().getPlayers()[(getModelView().getMyId() + 2) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                        ChosenAssistant3.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente" + getModelView().getPlayers()[(getModelView().getMyId() + 2) % getModelView().getPlayers().length].getUsedAssistants()[getModelView().getPlayers()[(getModelView().getMyId() + 2) % getModelView().getPlayers().length].getUsedAssistants().length - 1] + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    } else {
                        ChosenAssistant3.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago" + ((getModelView().getMyId() + 2) % getModelView().getPlayers().length+1) + ".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    }
                }

                if (getModelView().getPlayers().length == 4) {
                    money4.setText(((Integer) getModelView().getPlayers()[(getModelView().getMyId() + 3) % getModelView().getPlayers().length].getCoins()).toString());
                    if (getModelView().getPlayers()[(getModelView().getMyId() + 3) % getModelView().getPlayers().length].getUsedAssistants().length > 0) {
                        ChosenAssistant4.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\assistente"+getModelView().getPlayers()[(getModelView().getMyId() + 3) % getModelView().getPlayers().length].getUsedAssistants()[getModelView().getPlayers()[(getModelView().getMyId() + 3) % getModelView().getPlayers().length].getUsedAssistants().length-1]+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    } else {
                        ChosenAssistant4.setBackground(new Background(new BackgroundImage(new Image("/images\\assistenti\\mago"+((getModelView().getMyId() + 3) % getModelView().getPlayers().length+1)+".png"), BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(0.1, 0.1, true, true, false, true))));
                    }
                }
            }
        );
    }
}
