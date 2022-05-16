package it.polimi.ingsw.clientModels;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;

public class ClientModelDeSerializer {

    private static final Gson gson = new Gson();

    public static ClientModel deserialize(String json) throws NullPointerException {
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        switch(type){
            case "ClientBoard":
                return gson.fromJson(json, ClientBoard.class);
            case "ClientPlayer":
                return gson.fromJson(json, ClientPlayer.class);
            case "ClientIsle":
                return gson.fromJson(json, ClientIsle.class);
            case "ClientGameModel":
                return gson.fromJson(json, ClientGameModel.class);
            case "ClientCloud":
                return gson.fromJson(json, ClientCloud.class);
            case "StartMessage":
                return gson.fromJson(json, StartMessage.class);
            case "CharacterModel":
                return gson.fromJson(json, ClientCharacter.class);
            case "TurnMessage":
                return gson.fromJson(json, TurnMessage.class);
            default:
                throw new NullPointerException();
        }
    }
}
