package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * UML CHANGES <br>
 * 1) Added function getWinner() <br>
 */

public class Game {

    private final static int MAX_N_ISLES = 12;

    private GameModel gameModel; // WHEN TO INITIALIZE???
    private ActionTurnHandler turn;
    private boolean started;
    private List<Integer> playersOrder;
    private int currentPlayer;

    public void setGameModelDEBUG(GameModel g){
        this.gameModel = g;
    }

    public Player getWinner(){
        return gameModel.getPlayers().stream()
                .filter(p -> (
                        p.getBoard().getTowers() == gameModel.getPlayers().stream()
                                .map(n -> n.getBoard().getTowers())
                                .min(Integer::compareTo)
                                .get()
                        ))
                .max(Comparator.comparingInt(a -> gameModel.numberOfProfessors(a)))
                .get();
    }

    // DOESN'T SAY WHO WINS
    public boolean checkEnd(){
        // TOWER FINISHED
        for(Player p : gameModel.getPlayers()){
            if(p.getBoard().getTowers() <= 0){
                return true;
            }
        }
        // MORE THAN 3 ISLES
        if(gameModel.getIsles().size() <= 3){
            return true;
        }
        // NO STUDENTS IN BAG
        if(gameModel.checkEmptyBag()){
            return true;
        }
        // NO MORE ASSISTANTS
        for(Player p : gameModel.getPlayers()){
            if(p.getDeck().size() <= 0){
                return true;
            }
        }
        return false;
    }

    public void giveCoin(Player p){
        try{
            gameModel.removeCoin();
            gameModel.getPlayer(p.getID()).addCoin();
        }catch(NotEnoughCoinException e){
            // WHAT??? TELL THE PLAYER THE COINS ARE FINISHED?
        }
    }

    // EXPECTED ALL PLAYERS CREATED
    public void setupGame(int numOfPLayers){
        ArrayList<Student> tempStud = new ArrayList<>();
        for(Colour c : Colour.values()){
            tempStud.add(new Student(c));
            tempStud.add(new Student(c));
        }
        Random rand = new Random();
        try {
            gameModel.setMotherNPos(rand.nextInt(MAX_N_ISLES));
        } catch (TileOutOfBoundsException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < MAX_N_ISLES; i++){
            if(i != gameModel.getMotherNature() && i != (gameModel.getMotherNature() + MAX_N_ISLES /2) % MAX_N_ISLES){
                gameModel.getIsle(i).addStudent(tempStud.remove(rand.nextInt(MAX_N_ISLES - 2)));
            }
        }

        for(Cloud c: gameModel.getClouds())
        {
            c.addStudents(gameModel.extractStudents((numOfPLayers == 3)?4:3));
        }

        for(Player p : gameModel.getPlayers()){
            p.createBoard((numOfPLayers == 3)?6:8);
            p.getBoard().addStudents(gameModel.extractStudents((numOfPLayers==3)?9:7));
        }

        currentPlayer = rand.nextInt(gameModel.getPlayers().size());
        // TURN HANDLER??? I DON'T REMEMBER...
    }

    public GameModel getGameModel(){
        return gameModel;
    }

    public Player createPLayer(String nickname, int ID){
        return new Player(ID, nickname);
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void chooseAssistant(int assistantID, int playerID){
        String notify = null;
        try{
            gameModel.getPlayer(playerID).setChoosenAssistant(assistantID);
        }catch (IndexOutOfBoundsException e){
            notify = "Assistant choosen id must be less than 10 and more than 0";
        }
    }

    public void checkNextOrder(){
        ArrayList<Player> tempPlayers = new ArrayList<>(gameModel.getPlayers());
        tempPlayers.sort(Comparator.comparingInt(p -> p.getChosen().getValue()));
        playersOrder = tempPlayers.stream()
                .map(Player::getID)
                .collect(Collectors.toList());
    }

}
