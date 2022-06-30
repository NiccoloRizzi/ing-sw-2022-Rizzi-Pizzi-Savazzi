package it.polimi.ingsw.clientModels;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;

/**
 * Class for Deserializing messages from Json format to Java Classes
 */
public class ClientModelDeSerializer {
    /**
     * Gson instance used to serialize to json
     */
    private static final Gson gson = new Gson();

    /**
     * Deserializing method which handles inheritance
     * @param json The message in JSON format
     * @return Deserialized ClientModel
     * @throws NullPointerException When given a generic instance of an object with ClientModel interface
     */
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
