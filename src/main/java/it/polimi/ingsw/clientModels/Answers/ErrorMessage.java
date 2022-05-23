package it.polimi.ingsw.clientModels.Answers;


import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.ModelSerializer;

public class ErrorMessage implements ClientModel {

    private int id;
    private ErrorType error;

    public ErrorMessage(int id,ErrorType error)
    {
        this.id = id;
        this.error = error;
    }
    @Override
    public void accept(View visitor)
    {
        visitor.visit(this);
    }

    public ErrorType getError() {
        return error;
    }

    public int getId(){return id;}

    public enum ErrorType {
        NotYourTurnError("Non è il tuo turno"),
        NotEnoughCoinError("Non hai abbastanza monete"),
        CloudError("La nuvola è già stata scelta"),
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
        NicknameTaken("Nickname già preso");

        private final String errorMsg;

        ErrorType(String errorMsg){
            this.errorMsg = errorMsg;
        }

        public String getErrorMsg() {
            return errorMsg;
        }
    }

    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }
}
