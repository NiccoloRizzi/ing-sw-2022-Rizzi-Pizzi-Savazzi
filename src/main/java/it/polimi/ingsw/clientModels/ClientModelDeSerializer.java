package it.polimi.ingsw.clientModels;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;

public class ClientModelDeSerializer {

    private static final Gson gson = new Gson();

    public static ClientModel deserialize(String json) throws NullPointerException {
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        return switch (type) {
            case "ClientBoard" -> gson.fromJson(json, ClientBoard.class);
            case "ClientPlayer" -> gson.fromJson(json, ClientPlayer.class);
            case "ClientIsle" -> gson.fromJson(json, ClientIsle.class);
            case "ClientGameModel" -> gson.fromJson(json, ClientGameModel.class);
            case "ClientCloud" -> gson.fromJson(json, ClientCloud.class);
            case "StartMessage" -> gson.fromJson(json, StartMessage.class);
            case "CharacterModel" -> gson.fromJson(json, ClientCharacter.class);
            case "TurnMessage" -> gson.fromJson(json, TurnMessage.class);
            case "ErrorMessage" -> gson.fromJson(json, ErrorMessage.class);
            case "WinMessage" -> gson.fromJson(json, WinMessage.class);
            default -> throw new NullPointerException();
        };
    }
}
