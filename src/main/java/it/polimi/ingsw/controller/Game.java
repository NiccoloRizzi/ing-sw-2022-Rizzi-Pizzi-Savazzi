package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;

import java.lang.reflect.Array;
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
    private boolean planningPhase;
    private int playersNumber;
    private ArrayList<Integer> planningOrder;
    private ArrayList<Integer> actionOrder;
    private int currentPlayer;
    private boolean expertMode;

    public Game(int playersNumber, boolean expertMode){
        this.playersNumber= playersNumber;
        gameModel = new GameModel(playersNumber);
        this.expertMode = expertMode;
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

    public void startActionTurn(){
       turn = new ActionTurnHandler(currentPlayer,gameModel, playersNumber);
    }

    public boolean isPlanning(){
        return planningPhase;
    }

    public void giveCoin(Player p){
//        try{
//            gameModel.removeCoin();
//            gameModel.getPlayer(p.getID()).addCoin();
//        }catch(NotEnoughCoinsException e){
//            // WHAT??? TELL THE PLAYER THE COINS ARE FINISHED?
//        }
    }

    public ArrayList<Integer> getPlanningOrder() {
        return planningOrder;
    }

    public boolean alreadyUsed(int assistantId){
        ArrayList<Player> players = gameModel.getPlayers();
        for (int i = 0; i < planningOrder.indexOf(currentPlayer); i++) {
            if (players.get(planningOrder.get(i)).getChosen().getValue() == assistantId+1) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getActionOrder(){
        return actionOrder;
    }

    // EXPECTED ALL PLAYERS CREATED
    public void setupGame(){
        planningOrder = new ArrayList<>();
        actionOrder = new ArrayList<>();
        ArrayList<Colour> students = new ArrayList<>();
        for(Colour c: Colour.values()){
            students.add(c);
            students.add(c);
        }
        Random rand = new Random();
        try {
            gameModel.setMotherNPos(rand.nextInt(MAX_N_ISLES));
        } catch (TileOutOfBoundsException e) {
            e.printStackTrace();
        }

        for(int i=0; i<MAX_N_ISLES && students.size()>0; i++) {
                int student = rand.nextInt(students.size());
                try {
                    if (!(gameModel.getMotherNature() == i || (gameModel.getMotherNature() + 6) % 12 == i)) {
                        gameModel.getIsle(i).addStudent(students.remove(student));
                    }
                } catch (TileOutOfBoundsException e) {
                    e.printStackTrace();
                }
        }

        for(Cloud c: gameModel.getClouds())
        {
            try {
                c.addStudents(gameModel.extractStudents((playersNumber == 3) ? 4 : 3));
            }catch(StudentsOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        for(Player p : gameModel.getPlayers()){
            p.createBoard((playersNumber == 3)?6:8);
            try {
                p.getBoard().addStudents(gameModel.extractStudents((playersNumber == 3) ? 9 : 7));
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        currentPlayer = rand.nextInt(playersNumber);
        for(int i=0;i<playersNumber;i++){
            planningOrder.add((currentPlayer+i)%playersNumber);
        }
        planningPhase = true;
    }

    public boolean isExpertMode(){
        return expertMode;
    }

    public GameModel getGameModel(){
        return gameModel;
    }

    public void createPlayer(String nickname){
        gameModel.addPlayer(gameModel.getPlayers().size(),nickname);
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void chooseAssistant(int assistantID, int playerID){
        String notify = null;
        try{
            gameModel.getPlayer(playerID).setChoosenAssistant(assistantID);
            nextPlayer();
        }catch (IndexOutOfBoundsException e){
            notify = "Assistant chosen id must be less than 10 and more than 0";
        }
    }

    public void checkNextOrder(){
        ArrayList<Player> tempPlayers = new ArrayList<>(gameModel.getPlayers());
        tempPlayers.sort(Comparator.comparingInt(p -> p.getChosen().getValue()));
        actionOrder = tempPlayers.stream()
                .map(player -> player.getID())
                .collect(Collectors.toCollection(ArrayList<Integer>::new));

        int first = actionOrder.get(0);
        for(int i=1;i<playersNumber;i++){
            planningOrder.set(i,(first+i)%playersNumber);
        }
    }

    public void nextPlayer(){
        if(planningPhase){
            if(planningOrder.indexOf(currentPlayer)==planningOrder.size()-1){
                planningPhase = false;
                checkNextOrder();
                currentPlayer = actionOrder.get(0);
                startActionTurn();
            }
            else{
                currentPlayer=planningOrder.get(planningOrder.indexOf(currentPlayer)+1);
            }
        }
        else{
            if(currentPlayer==actionOrder.size()-1){
                currentPlayer = planningOrder.get(0);
                planningPhase = true;
            }
            else {
                currentPlayer = actionOrder.get(actionOrder.indexOf(currentPlayer) + 1);
                startActionTurn();
            }
        }
    }

    public ActionTurnHandler getTurnHandler()
    {
        return turn;
    }

}
