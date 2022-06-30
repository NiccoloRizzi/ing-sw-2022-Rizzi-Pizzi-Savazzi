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
        NotYourTurnError("Non è il momento giusto per fare questa azione"),
        NotEnoughCoinError("Non hai abbastanza monete"),
        CloudError("La nuvola è già stata scelta o non è valida"),
        AssistantOtherPlayerError("L'assistente è già stato scelto da un altro giocatore"),
        AssistantAlreadyChosenError("Hai già usato questo assistente"),
        CharacterAlreadyUsedError("Hai già usato un personaggio in questo turno"),
        NormalModeError("Non puoi usare funzioni della modalità esperto"),
        StudentError("Non puoi spostare lo studente di questo colore"),
        IsleError("L'isola scelta non esiste"),
        TileIsEmptyError("Stai provando a spostare studenti da un pinto in cui non ci sono"),
        TileIsFullError("Stai provando a spostare studenti verso un punto che non ammette altri studenti"),
        ProhibitedError("Non puoi più mettere tessere divieto perché sono finite"),
        MovesError("Le tue mosse devono essere coerenti con la carta assistente scelta"),
        PlayerDisconnected("Un giocatore si è disconnesso"),
        NicknameTaken("Nickname già preso"),
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
