package it.polimi.ingsw.clientModels;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
            default:
                throw new NullPointerException();
        }
    }
}
