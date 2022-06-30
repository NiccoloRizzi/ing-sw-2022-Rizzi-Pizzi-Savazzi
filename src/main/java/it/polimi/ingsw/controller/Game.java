package it.polimi.ingsw.controller;

import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Main Controller class that creates and handles the match
 */
public class Game extends Observable<ClientModel> {

    private final static int MAX_N_ISLES = 12;

    /**
     * The gamemodel representing the current match
     */
    private final GameModel gameModel;
    /**
     * The handler that handles players' action turns
     */
    private final ActionTurnHandler turn;
    /**
     * Boolean that tells if the game is currently in planning phase
     */
    private boolean planningPhase;
    /**
     * Number of players in the current match
     */
    private final int playersNumber;
    /**
     * The order of the players for the planning phase
     */
    private ArrayList<Integer> planningOrder;
    /**
     * The order of the players for the action phase
     */
    private ArrayList<Integer> actionOrder;
    /**
     * The current player
     */
    private int currentPlayer;
    /**
     * Boolean that saves if the current match is in expert mode
     */
    private final boolean expertMode;

    /**
     * Creates the current match based on the number of players and game mode
     * @param playersNumber the number of players
     * @param expertMode Whether the game is in expert mode
     */
    public Game(int playersNumber, boolean expertMode){
        this.playersNumber= playersNumber;
        gameModel = new GameModel(playersNumber,expertMode);
        this.expertMode = expertMode;
        turn = new ActionTurnHandler(gameModel, this.playersNumber);
    }

    /**
     * Returns the winner, if present
     * @return the winner
     */
    public Player getWinner(){
        Player winner = gameModel.getPlayers().stream()
                .filter(p -> (
                        p.getBoard().getTowers() == gameModel.getPlayers().stream()
                                .map(n -> n.getBoard().getTowers())
                                .min(Integer::compareTo)
                                .get()
                        ))
                .max(Comparator.comparingInt(gameModel::numberOfProfessors))
                .get();
        Optional<Player> check = gameModel.getPlayers().stream()
                .filter(p -> (
                        p.getBoard().getTowers() == gameModel.getPlayers().stream()
                                .map(n -> n.getBoard().getTowers())
                                .min(Integer::compareTo)
                                .get()
                        && p.getID()!= winner.getID()
                ))
                .max(Comparator.comparingInt(gameModel::numberOfProfessors));
        if(check.isPresent() && winner.getBoard().getTowers() == check.get().getBoard().getTowers() && gameModel.numberOfProfessors(winner) == gameModel.numberOfProfessors(check.get())){
            notify(new WinMessage(true));
        }else{
            notify(new WinMessage(winner.getID()));
        }
        return winner;
    }

    /**
     * Checks whether end conditions for lack of towers or number of islands are met
     * @return whether ending conditions are met
     */
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

    /**
     * Checks whether end conditions for lack of students or assistants are met
     * @return whether ending conditions are met
     */
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

    /**
     * Ends the planning phase and starts the action turn for the first player
     */
    public void startActionTurn(){
        planningPhase=false;
       turn.setupActionTurnHandler(currentPlayer);
    }

    /**
     *
     * @return whether the game is currently in planning phase
     */
    public boolean isPlanning(){
        return planningPhase;
    }

    /**
     *
     * @return players' order during planning phase
     */
    public ArrayList<Integer> getPlanningOrder() {
        return planningOrder;
    }

    /**
     * Checks whether another player has used a given assistant during this round
     * @param assistantId the assistant to check
     * @return whether the assistant has been already used in this round
     */
    public boolean alreadyUsed(int assistantId){
        ArrayList<Player> players = gameModel.getPlayers();
        for (int i = 0; i < planningOrder.indexOf(currentPlayer); i++) {
            if (players.get(planningOrder.get(i)).getChosen().getValue() == assistantId) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return the players' order for the action phase
     */
    public ArrayList<Integer> getActionOrder(){
        return actionOrder;
    }

    /**
     * Sets up and starts the game by creating all match elements
     */
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

    /**
     * Getter for expertMode
     * @return whether the game is in expert mode
     */
    public boolean isExpertMode(){
        return expertMode;
    }

    /**
     * Getter for GameModel
     * @return the game model of the current match
     */
    public GameModel getGameModel(){
        return gameModel;
    }

    /**
     * Adds a player to the current match
     * @param nickname the nickname of the player
     * @throws PlayerOutOfBoundException when players' limit is reached
     */
    public void createPlayer(String nickname) throws PlayerOutOfBoundException {
        gameModel.addPlayer(gameModel.getPlayers().size(),nickname);
    }

    /**
     *
     * @return the current player
     */
    public int getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * Determines the players' orders (planning and action) for the next round, based on chosen assistants
     */
    public void checkNextOrder(){
        ArrayList<Player> tempPlayers = new ArrayList<>(gameModel.getPlayers());
        tempPlayers.sort(Comparator.comparingInt(p -> p.getChosen().getValue()));
        actionOrder = tempPlayers.stream()
                .map(Player::getID)
                .collect(Collectors.toCollection(ArrayList<Integer>::new));

        int first = actionOrder.get(0);
        planningOrder.set(0,first);
        for(int i=1;i<playersNumber;i++){
            planningOrder.set(i,(first+i)%playersNumber);
        }
    }

    /**
     * Moves to the next player and sets up the turn accordingly
     */
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
                if(!checkEndStudentAssistant()) {
                    gameModel.fillClouds();
                    notify(new TurnMessage(currentPlayer, TurnMessage.Turn.PLANNING));
                }
            }
            else {
                currentPlayer = actionOrder.get(actionOrder.indexOf(currentPlayer) + 1);
                startActionTurn();
                notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_STUDENTS));
            }
        }
    }

    /**
     *
     * @return The number of players in the current match
     */
    public int getPlayersNumber(){
        return playersNumber;
    }

    /**
     *
     * @return The action turn handler
     */
    public ActionTurnHandler getTurnHandler()
    {
        return turn;
    }

    /**
     * Sets a given player as current
     * @param currentPlayer The player to be set
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void addObserver(Observer<ClientModel> observer){
        super.addObserver(observer);
        turn.addObserver(observer);
        gameModel.addObserver(observer);
    }

    /**
     * Sends the initial state of the game as an update
     */
    public void sendInitialGame(){
        gameModel.sendFullModel();
        notify(new TurnMessage(currentPlayer, TurnMessage.Turn.PLANNING));
    }
}
