package it.polimi.ingsw.controller;

import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * UML CHANGES <br>
 * 1) Added function getWinner() <br>
 */

public class Game extends Observable<ClientModel> {

    private final static int MAX_N_ISLES = 12;

    private final GameModel gameModel; // WHEN TO INITIALIZE???
    private final ActionTurnHandler turn;
    private boolean planningPhase;
    private final int playersNumber;
    private ArrayList<Integer> planningOrder;
    private ArrayList<Integer> actionOrder;
    private int currentPlayer;
    private final boolean expertMode;

    public Game(int playersNumber, boolean expertMode){
        this.playersNumber= playersNumber;
        gameModel = new GameModel(playersNumber,expertMode);
        this.expertMode = expertMode;
        turn = new ActionTurnHandler(gameModel);
    }

    public Player getWinner(){
        Player winner = gameModel.getPlayers().stream()
                .filter(p -> (
                        p.getBoard().getTowers() == gameModel.getPlayers().stream()
                                .map(n -> n.getBoard().getTowers())
                                .min(Integer::compareTo)
                                .get()
                        ))
                .max(Comparator.comparingInt(a -> gameModel.numberOfProfessors(a)))
                .get();
        Optional<Player> check = gameModel.getPlayers().stream()
                .filter(p -> (
                        p.getBoard().getTowers() == gameModel.getPlayers().stream()
                                .map(n -> n.getBoard().getTowers())
                                .min(Integer::compareTo)
                                .get() && p.getID()!= winner.getID()
                ))
                .max(Comparator.comparingInt(a -> gameModel.numberOfProfessors(a)));
        if(check.isPresent() && winner.getBoard().getTowers() == check.get().getBoard().getTowers() && gameModel.numberOfProfessors(winner) == gameModel.numberOfProfessors(check.get())){
            notify(new WinMessage(true));
        }else{
            notify(new WinMessage(winner.getID()));
        }
        return winner;
    }

    // DOESN'T SAY WHO WINS
    public boolean checkEndTowerIsle() {
        // TOWER FINISHED
        for (Player p : gameModel.getPlayers()) {
            if (p.getBoard().getTowers() <= 0) {
                getWinner();
                return true;
            }
        }
        // MORE THAN 3 ISLES
        if (gameModel.getIsles().size() <= 3) {
            getWinner();
            return true;
        }
        return false;
    }
    public boolean checkEndStudentAssistant(){
        // NO STUDENTS IN BAG
        if(gameModel.checkEmptyBag()){
            getWinner();
            return true;
        }
        // NO MORE ASSISTANTS
        for(Player p : gameModel.getPlayers()){
            if(p.getDeck().size() <= 0){
                getWinner();
                return true;
            }
        }
        return false;
    }

    public void startActionTurn(){
        planningPhase=false;
       turn.setupActionTurnHandler(currentPlayer, playersNumber);
    }

    public boolean isPlanning(){
        return planningPhase;
    }

    public ArrayList<Integer> getPlanningOrder() {
        return planningOrder;
    }

    public boolean alreadyUsed(int assistantId){
        ArrayList<Player> players = gameModel.getPlayers();
        for (int i = 0; i < planningOrder.indexOf(currentPlayer); i++) {
            if (players.get(planningOrder.get(i)).getChosen().getValue() == assistantId) {
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
            gameModel.giveCoin(p);
            try {
                p.getBoard().addStudents(gameModel.extractStudents((playersNumber == 3) ? 9 : 7));
            }catch(StudentsOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        if(playersNumber == 4)
        {
            gameModel.getTeam(0).assignFaction(Faction.Black);
            gameModel.getTeam(1).assignFaction(Faction.White);
        }else{
            for(int i = 0;i<gameModel.getPlayers().size();i++)
                gameModel.getPlayer(i).assignFaction(Faction.values()[i+1]);
        }

        currentPlayer = rand.nextInt(playersNumber);
        for(int i=0;i<playersNumber;i++){
            planningOrder.add((currentPlayer+i)%playersNumber);
        }
        if(expertMode)
            gameModel.setUpCharacters();
        planningPhase = true;
    }

    public boolean isExpertMode(){
        return expertMode;
    }

    public GameModel getGameModel(){
        return gameModel;
    }

    public void createPlayer(String nickname) throws PlayerOutOfBoundException {
        gameModel.addPlayer(gameModel.getPlayers().size(),nickname);
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void checkNextOrder(){
        ArrayList<Player> tempPlayers = new ArrayList<>(gameModel.getPlayers());
        tempPlayers.sort(Comparator.comparingInt(p -> p.getChosen().getValue()));
        actionOrder = tempPlayers.stream()
                .map(player -> player.getID())
                .collect(Collectors.toCollection(ArrayList<Integer>::new));

        int first = actionOrder.get(0);
        planningOrder.set(0,first);
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
                notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_STUDENTS));
            }
            else{
                currentPlayer=planningOrder.get(planningOrder.indexOf(currentPlayer)+1);
                notify(new TurnMessage(currentPlayer, TurnMessage.Turn.PLANNING));
            }
        }
        else{
            if(currentPlayer==actionOrder.get(actionOrder.size()-1)){
                currentPlayer = planningOrder.get(0);
                planningPhase = true;
                gameModel.fillClouds();
                if(!checkEndStudentAssistant())
                    notify(new TurnMessage(currentPlayer, TurnMessage.Turn.PLANNING));
            }
            else {
                currentPlayer = actionOrder.get(actionOrder.indexOf(currentPlayer) + 1);
                startActionTurn();
                notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_STUDENTS));
            }
        }
    }

    public int getPlayersNumber(){
        return playersNumber;
    }

    public ActionTurnHandler getTurnHandler()
    {
        return turn;
    }
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void addObserver(Observer<ClientModel> observer){
        super.addObserver(observer);
        turn.addObserver(observer);
        gameModel.addObserver(observer);
    }

    public void sendInitialGame(){
        gameModel.sendFullModel();
        notify(new TurnMessage(currentPlayer, TurnMessage.Turn.PLANNING));
    }
}
