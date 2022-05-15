package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.exceptions.EmptyMessageException;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;

public class ModelSerializer {
    private static final Gson gson = new Gson();

    public static String serialize(ClientModel cm) throws EmptyMessageException {
        System.out.println(cm.getClass());
        throw new EmptyMessageException();
    }

    public static String serialize(ClientBoard cb){
        String json = gson.toJson(cb);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientBoard");
        return jo.toString();
    }

    public static String serialize(ClientCloud cc){
        String json = gson.toJson(cc);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientCloud");
        return jo.toString();
    }

    public static String serialize(ClientGameModel cgm){
        String json = gson.toJson(cgm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientGameModel");
        return jo.toString();
    }

    public static String serialize(ClientIsle ci){
        String json = gson.toJson(ci);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientIsle");
        return jo.toString();
    }

    public static String serialize(ClientPlayer cp){
        String json = gson.toJson(cp);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientPlayer");
        return jo.toString();
    }

    public static String serialize(ErrorMessage error){
        String json = gson.toJson(error);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ErrorMessage");
        return jo.toString();
    }

    public static String serialize(StartMessage sm){
        String json = gson.toJson(sm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","StartMessage");
        return jo.toString();
    }

    public static String serialize(ClientCharacter cm){
        String json = gson.toJson(cm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","CharacterModel");
        return jo.toString();
    }

    public static String serialize(TurnMessage tm){
        String json = gson.toJson(tm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","TurnMessage");
        return jo.toString();
    }

    public static String serialize(WinMessage wm){
        String json = gson.toJson(wm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","WinMessage");
        return jo.toString();
    }
}
