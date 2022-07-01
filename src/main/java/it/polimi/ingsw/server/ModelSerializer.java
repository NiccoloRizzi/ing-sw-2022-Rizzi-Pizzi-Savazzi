package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.clientModels.Answers.TurnMessage;
import it.polimi.ingsw.clientModels.Answers.WinMessage;
import it.polimi.ingsw.exceptions.EmptyMessageException;
import it.polimi.ingsw.clientModels.Answers.ErrorMessage;

/**
 * Class that serializes the type of updates the server can send to the clients
 */
public class ModelSerializer {
    /**Gson needed to serialize the message**/
    private static final Gson gson = new Gson();

    /** Default serialize method that lunch an exception because tries to serialize a generic interface
     *
     * @param cm interface
     * @throws EmptyMessageException because cm is an interface
     */
    public static void serialize(ClientModel cm) throws EmptyMessageException {
        System.out.println(cm.getClass());
        throw new EmptyMessageException();
    }

    /**
     * Method to serialize the message
     * @param cb ClientBoard to serialize
     * @return serialized message
     */
    public static String serialize(ClientBoard cb){
        String json = gson.toJson(cb);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientBoard");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param cc ClientCloud to serialize
     * @return serialized message
     */
    public static String serialize(ClientCloud cc){
        String json = gson.toJson(cc);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientCloud");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param cgm ClientGameModel to serialize
     * @return serialized message
     */
    public static String serialize(ClientGameModel cgm){
        String json = gson.toJson(cgm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientGameModel");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param ci ClientIsle to serialize
     * @return serialized message
     */
    public static String serialize(ClientIsle ci){
        String json = gson.toJson(ci);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientIsle");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param cp ClientPLayer to serialize
     * @return serialized message
     */
    public static String serialize(ClientPlayer cp){
        String json = gson.toJson(cp);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ClientPlayer");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param error ErrorMessage to serialize
     * @return serialized message
     */
    public static String serialize(ErrorMessage error){
        String json = gson.toJson(error);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","ErrorMessage");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param sm StartMessage to serialize
     * @return serialized message
     */
    public static String serialize(StartMessage sm){
        String json = gson.toJson(sm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","StartMessage");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param cm ClientCharacter to serialize
     * @return serialized message
     */
    public static String serialize(ClientCharacter cm){
        String json = gson.toJson(cm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","CharacterModel");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param tm TurnMessage to serialize
     * @return serialized message
     */
    public static String serialize(TurnMessage tm){
        String json = gson.toJson(tm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","TurnMessage");
        return jo.toString();
    }
    /**
     * Method to serialize the message
     * @param wm WinMessage to serialize
     * @return serialized message
     */
    public static String serialize(WinMessage wm){
        String json = gson.toJson(wm);
        JsonObject jo = gson.fromJson(json,JsonObject.class);
        jo.addProperty("type","WinMessage");
        return jo.toString();
    }
}
