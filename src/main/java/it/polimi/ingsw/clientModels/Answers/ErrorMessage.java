package it.polimi.ingsw.clientModels.Answers;


import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;

public class ErrorMessage implements ClientModel {

    private ErrorType error;

    public ErrorMessage(ErrorType error)
    {
        this.error = error;
    }
    @Override
    public void accept(View visitor)
    {
        visitor.visit(this);
    }

    public enum ErrorType {
        NotYourTurnError,
        NotEnoughCoinError,
        CloudError,
        AssistantOtherPlayerError,
        AssistantAlreadyChosenError,
        CharacterAlreadyUsedError,
        NormalModeError,
        StudentError,
        IsleError,
        TileIsEmptyError,
        TileIsFullError,
        ProhibitedError,
        MovesError
    }
}
