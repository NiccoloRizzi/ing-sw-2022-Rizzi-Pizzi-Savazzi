package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientBoard;
import it.polimi.ingsw.client.ClientIsle;
import it.polimi.ingsw.client.ClientModel;
import it.polimi.ingsw.client.ClientPlayer;

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
            default:
                throw new NullPointerException();
        }
    }
}
