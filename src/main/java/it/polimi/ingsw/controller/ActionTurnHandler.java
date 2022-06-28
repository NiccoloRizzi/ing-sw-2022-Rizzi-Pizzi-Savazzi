package it.polimi.ingsw.controller;

import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.NotEnoughCoinsException;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TileOutOfBoundsException;
import it.polimi.ingsw.model.GameModel;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

import java.util.List;
import java.util.Optional;

/**
 * Handles the actiorn turn of each player
 */
public class ActionTurnHandler extends Observable<ClientModel> {
    private int currentPlayer;
    private final GameModel gameModel;
    /**
     * How many students the player can and must move
     */
    private int studentsToMove;
    /**
     * Current strategy for checking who owns a professor
     */
    private CheckProfessorStrategy professorStrategy;
    /**
     * Current strategy for checking who owns an Isle
     */
    private CheckTowerStrategy checkTowerStrategy;
    /**
     * Current phase of the action turn
     */
    private Phase phase;
    /**
     * Boolean that saves if the player has used a character for this turn
     */
    private boolean usedCharacter;

    /**
     * Creates the action turn handler with a given gameModel
     * @param gameModel the gameModel of the match
     */
    public ActionTurnHandler(GameModel gameModel){
        this.gameModel = gameModel;

    }

    /**
     * Sets up the ActionTurnHandler to handle the current player for the current match
     * @param currentPlayer The current player
     * @param numOfPlayers The number of players
     */
    public void setupActionTurnHandler(int currentPlayer,int numOfPlayers){
        this.currentPlayer = currentPlayer;
        studentsToMove = (numOfPlayers == 3)?4:3;
        phase = Phase.STUDENTS;
        professorStrategy= new DefaultCheckProfessorStrategy();
        checkTowerStrategy = (numOfPlayers == 4)? new TeamCheckTowerStrategy() : new PlayerCheckTowerStrategy();
        usedCharacter = false;
    }

    /**
     * Moves mother nature and checks for changes in isles (towers and joined isles)
     * @param moves The amount of moves mother nature will be moved
     */
    public void moveMn(int moves){
        Assistant a = gameModel.getPlayers().get(currentPlayer).getChosen();

        if(moves<=a.getMn_moves()+a.getBoost() && moves>0){
            gameModel.moveMN(moves);
            checkTower(gameModel.getMotherNature());
            checkIsleJoin(gameModel.getMotherNature());
            phase=Phase.CLOUD;
            notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_CLOUDS));
        }
        else{
            notify(new ErrorMessage(currentPlayer,ErrorMessage.ErrorType.MovesError));
        }
        setInfStrategy(new DefaultInfStrategy());
    }

    /**
     * Sets the current strategy for calculating influence
     * @param strategy The strategy to apply
     */
    public void setInfStrategy(influenceStrategy strategy){
        for(Isle isle : gameModel.getIsles()){
            isle.setInfStrategy(strategy);
        }
    }

    /**
     * Moves a student from the current player's board to an island
     * @param student The colour of the student
     * @param isle The isle where the student will be moved to
     */
    public void moveStudentToIsle(Colour student, int isle){
        Player player = gameModel.getPlayer(currentPlayer);
        try{
            if(player.getBoard().getStudents(student)>0){
                if(isle < gameModel.getIsles().size() && isle>=0) {
                    player.getBoard().removeStudent(student);
                    gameModel.getIsle(isle).addStudent(student);
                    studentsToMove --;
                    if(studentsToMove  == 0) {
                        phase = Phase.MOTHERNATURE;
                        notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_MN));
                    }
                }
                else{
                    notify(new ErrorMessage(currentPlayer,ErrorMessage.ErrorType.IsleError));
                }
            }
            else {
                notify(new ErrorMessage(currentPlayer,ErrorMessage.ErrorType.StudentError));
            }
        }catch(StudentsOutOfBoundsException e){
            e.printStackTrace();
        }catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }
    }

    /**
     * Moves a student from the current player's board to his table
     * @param student
     */
    public void moveStudentToTable(Colour student){
        try{
            Player player = gameModel.getPlayer(currentPlayer);
            if(player.getBoard().getStudents(student)>0) {
                if(!player.getBoard().isTableFull(student)) {
                    player.getBoard().removeStudent(student);
                    player.getBoard().addToTable(student);
                    if(player.getBoard().checkCoin(student))
                        gameModel.giveCoin(player);
                    checkProfessor(student);
                    studentsToMove --;
                    if(studentsToMove  == 0) {
                        phase = Phase.MOTHERNATURE;
                        notify(new TurnMessage(currentPlayer, TurnMessage.Turn.ACTION_MN));
                    }
                }
                else{
                    notify(new ErrorMessage(currentPlayer,ErrorMessage.ErrorType.TileIsFullError));
                }
            }else{
                notify(new ErrorMessage(currentPlayer,ErrorMessage.ErrorType.StudentError));
            }


        }catch(StudentsOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    /**
     * Empties a cloud and moves all its students to the current player's board
     * @param cloudId The chosen cloud id
     * @return Whether it was successful
     */
    public boolean moveFromCloud(int cloudId){

        try{
            if(cloudId <0 || cloudId>=gameModel.getClouds().size()) {
                notify(new ErrorMessage(currentPlayer, ErrorMessage.ErrorType.CloudError));
                return false;
            }
            else {
                if (gameModel.getCloud(cloudId).isEmpty()) {
                    notify(new ErrorMessage(currentPlayer, ErrorMessage.ErrorType.CloudError));
                    return false;
                } else {
                    try {
                        gameModel.getPlayer(currentPlayer).getBoard().addStudents(gameModel.getCloud(cloudId).empty());
                    } catch (TileOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(TileOutOfBoundsException e){
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Checks if an isle can be joined
     * @param index The id of the isle
     */
    public void checkIsleJoin(int index)
    {
        try {
            gameModel.joinIsle(index);
        }catch(TileOutOfBoundsException e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return The number of students the current player can still move
     */
    public int getStudentsToMove() {
        return studentsToMove;
    }

    /**
     * Returns the current phase
     * @return current phase
     */
    public Phase getPhase(){
        return phase;
    }

    /**
     * Sets the current strategy when choosing who owns a professor
     * @param strategy The strategy to apply
     */
     public void setProfessorStrategy(CheckProfessorStrategy strategy)
    {
        professorStrategy = strategy;
    }

    /**
     * Checks who should own a professor of a given colour
     * @param student The colour to check
     */
    public void checkProfessor(Colour student)
    {
        professorStrategy.checkProfessor(gameModel,student,currentPlayer);
    }

    /**
     * Checks who controls an isle
     * @param isle The isle to check
     */
    public void checkTower(int isle){
        checkTowerStrategy.checkTower(gameModel,isle);
    }

    /**
     * Changes current player
     * @param player The player to set as current
     */
    public void setCurrentPlayer(Player player)
    {
        this.currentPlayer = player.getID();
    }

    /**
     * Checks if a character has been used in this round
     * @return whether a character has been used
     */
    public boolean isUsedCharacter() {
        return usedCharacter;
    }

    /**
     * Sets usedCharacter to the given value
     * @param used Boolean value
     */
    public void setUsedCharacter (boolean used)
    {
        usedCharacter = used;
    }
}
