package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.messages.*;

/**
 * Class for deserializing players' moves into message objects
 */
public class MoveDeserializer {

    /**Gson needed to serialize the message**/
    private static final Gson gson = new Gson();


    /** Deserialize the string and return the instance of the specific class of the serialized message
     *
     * @param json serialized message
     * @return the instance of the Message interface
     */
    public static Message deserialize(String json){
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        return switch (type) {
            case "AssistantChoiceMessage" -> gson.fromJson(json, AssistantChoiceMessage.class);
            case "CloudChoiceMessage" -> gson.fromJson(json, CloudChoiceMessage.class);
            case "IsleInfluenceCharacterMessage" -> gson.fromJson(json, IsleInfluenceCharacterMessage.class);
            case "Move2StudCharacterMessage" -> gson.fromJson(json, Move2StudCharacterMessage.class);
            case "Move6StudCharacterMessage" -> gson.fromJson(json, Move6StudCharacterMessage.class);
            case "MoveMotherNatureMessage" -> gson.fromJson(json, MoveMotherNatureMessage.class);
            case "MoveStudentCharacterMessage" -> gson.fromJson(json, MoveStudentCharacterMessage.class);
            case "MoveStudentMessage" -> gson.fromJson(json, MoveStudentMessage.class);
            case "Plus2MoveMnMessage" -> gson.fromJson(json, Plus2MoveMnMessage.class);
            case "ProhibitedIsleCharacterMessage" -> gson.fromJson(json, ProhibitedIsleCharacterMessage.class);
            case "Remove3StudCharacterMessage" -> gson.fromJson(json, Remove3StudCharacterMessage.class);
            case "SimilMotherNatureMessage" -> gson.fromJson(json, SimilMotherNatureMesage.class);
            case "StrategyProfessorMessage" -> gson.fromJson(json, StrategyProfessorMessage.class);
            default -> null;
        };
    }


    /** Deserialize a player message
     *
     * @param json serialized message
     * @return instance of PLayerMessage
     */
    public static PlayerMessage deserializePlayerMessage(String json){
        JsonObject message = gson.fromJson(json, JsonObject.class);
        if (message.get("type").getAsString().equals("PlayerMessage")) {
            return gson.fromJson(json, PlayerMessage.class);
        }else{
            throw new IllegalArgumentException();
        }
    }

}
