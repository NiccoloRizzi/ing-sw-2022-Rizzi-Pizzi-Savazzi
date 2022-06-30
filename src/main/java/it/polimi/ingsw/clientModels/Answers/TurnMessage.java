package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.ModelSerializer;

/**
 * Message that conveys turn information to the players
 */
public class TurnMessage implements ClientModel {
    /**
     * The ID of the current player
     */
    private final int playerId;
    /**
     * The type of the current turn
     */
    private final Turn turn;

    /**
     * Constructor for TurnMessage
     * @param id Player's ID
     * @param turn Current turn type
     */
    public TurnMessage (int id, Turn turn){
        this.playerId = id;
        this.turn = turn;
    }

    /**
     * Enum for the types of Turn
     */
    public enum Turn{
        /**
         * Planning turn
         */
        PLANNING("Pianificazione, scelta assistente"),
        /**
         * Action turn, students phase
         */
        ACTION_STUDENTS("Azione, spostamento studenti"),
        /**
         * Action turn, move mothernature phase
         */
        ACTION_MN("Azione, spostamento madre natura"),
        /**
         * Action turn, choose cloud phase
         */
        ACTION_CLOUDS("Azione, scelta della nuvola");
        /**
         * Message associated with a turn
         */
        private final String turnMsg;

        /**
         * Enum constructor with the message associated to the type of turn
         * @param value The message
         */
        Turn(String value) {
            this.turnMsg = value;
        }

        /**
         * Getter for the message associated with the type of turn
         * @return Turn Message
         */
        public String getTurnMsg() {
            return turnMsg;
        }
    }
    /**
     * Accept method for visitor pattern
     * @param visitor The client-side visitor that handles server updates
     */
    public void accept(View visitor)
    {
        visitor.visit(this);
    }

    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }

    /**
     * Getter for player's ID
     * @return Player's ID
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * Getter for Turn
     * @return Turn
     */
    public Turn getTurn() {
        return turn;
    }
}
