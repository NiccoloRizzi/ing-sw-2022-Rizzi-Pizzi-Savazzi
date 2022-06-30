package it.polimi.ingsw.clientModels.Answers;


import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.ModelSerializer;

/**
 * Message sent to the players to convey errors
 */
public class ErrorMessage implements ClientModel {
    /**
     * The id of the player
     */
    private final int id;
    /**
     * The type of error
     */
    private final ErrorType error;

    /**
     * Constructor for an ErrorMessage
     * @param id The id of the player
     * @param error The type of error
     */
    public ErrorMessage(int id,ErrorType error)
    {
        this.id = id;
        this.error = error;
    }

    /**
     * Accept method for visitor pattern
     * @param visitor The client-side visitor that handles server updates
     */
    @Override
    public void accept(View visitor)
    {
        visitor.visit(this);
    }

    /**
     * Getter for error type
     * @return Error type
     */
    public ErrorType getError() {
        return error;
    }

    /**
     * Getter for player's id
     * @return Player's id
     */
    public int getId(){return id;}

    /**
     * Internal enum for error types
     */
    public enum ErrorType {
        /**
         * Error given when a player makes an action outside his turn
         */
        NotYourTurnError("Non è il momento giusto per fare questa azione"),
        /**
         * Error given when a player tries to use a character without having enough coins
         */
        NotEnoughCoinError("Non hai abbastanza monete"),
        /**
         * Error given when a player chooses an empty cloud or non-existing cloud
         */
        CloudError("La nuvola è già stata scelta o non è valida"),
        /**
         * Error given when a player chooses an assistant already chosen by another player in the round
         */
        AssistantOtherPlayerError("L'assistente è già stato scelto da un altro giocatore"),
        /**
         * Error given when a player chooses an assistant already used a previous turn
         */
        AssistantAlreadyChosenError("Hai già usato questo assistente"),
        /**
         * Error given when a player tries to use a character twice in the same turn
         */
        CharacterAlreadyUsedError("Hai già usato un personaggio in questo turno"),
        /**
         * Error given when the player tries to use Expert mode functions during a non-expert match
         */
        NormalModeError("Non puoi usare funzioni della modalità esperto"),
        /**
         * Error given when a player tries to move a student he doesn't have or can't move
         */
        StudentError("Non puoi spostare lo studente di questo colore"),
        /**
         * Error given when a player tries to interact with a non-existing isle
         */
        IsleError("L'isola scelta non esiste"),
        /**
         * Error given when a player tries to get a student from an empty tile
         */
        TileIsEmptyError("Stai provando a spostare studenti da un punto in cui non ci sono"),
        /**
         * Error given when a player tries to move a student to an empty tile
         */
        TileIsFullError("Stai provando a spostare studenti verso un punto che non ammette altri studenti"),
        /**
         * Error given when a player tries to use the prohibited character when there are no more prohibitions
         */
        ProhibitedError("Non puoi più mettere tessere divieto perché sono finite"),
        /**
         * Error given when a player tries to move mother nature for more steps than he can, or less than one
         */
        MovesError("Le tue mosse devono essere coerenti con la carta assistente scelta"),
        /**
         * Error given when a player disconnects abruptly
         */
        PlayerDisconnected("Un giocatore si è disconnesso"),
        /**
         * Error given when a tries to connect with a nickname already chosen by another player
         */
        NicknameTaken("Nickname già preso"),
        /**
         * Error given when a player tries to move more students than he can
         */
        WrongStudentNumber("Il numero di studenti selezionati è sbagliato");
        /**
         * Error Message associated with a particular type of error
         */
        private final String errorMsg;

        /**
         * Enum constructor for error type
         * @param errorMsg Error Message associated with a particular type of error
         */
        ErrorType(String errorMsg){
            this.errorMsg = errorMsg;
        }

        /**
         * Getter for error message
         * @return Error message
         */
        public String getErrorMsg() {
            return errorMsg;
        }
    }

    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }
}
