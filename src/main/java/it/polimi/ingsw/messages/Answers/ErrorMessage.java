package it.polimi.ingsw.messages.Answers;

public class ErrorMessage {

    private ErrorType error;


    public enum ErrorType {
        TurnError,
        CloudError,
        AssistantOtherPlayerError,
        AssistantAlreadyChosenError,
        CharacterError,
        StudentError,
        IsleError
    }
}
