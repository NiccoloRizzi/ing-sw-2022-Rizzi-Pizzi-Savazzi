package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.messages.*;

public class MessageSerializer {

    private static final Gson gson = new Gson();

    public static String serialize(AssistantChoiceMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","AssistantChoiceMessage");
        return jsonObject.toString();
    }

    public static String serialize(CloudChoiceMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","CloudChoiceMessage");
        return jsonObject.toString();
    }

    public static String serialize(IsleInfluenceCharacterMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","IsleInfluenceMessage");
        return jsonObject.toString();
    }

    public static String serialize(Move2StudCharacterMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","Move2StudCharacterMessage");
        return jsonObject.toString();
    }

    public static String serialize(Move6StudCharacterMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","Move6StudCharacterMessage");
        return jsonObject.toString();
    }

    public static String serialize(MoveMotherNatureMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","MoveMotherNatureMessage");
        return jsonObject.toString();
    }

    public static String serialize(MoveStudentCharacterMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","MoveStudentCharacterMessage");
        return jsonObject.toString();
    }

    public static String serialize(MoveStudentMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","MoveStudentMessage");
        return jsonObject.toString();
    }

    public static String serialize(Plus2MoveMnMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","Plus2MoveMnMessage");
        return jsonObject.toString();
    }

    public static String serialize(ProhibitedIsleCharacterMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","ProhibitedIsleCharacterMessage");
        return jsonObject.toString();
    }

    public static String serialize(Remove3StudCharacterMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","Remove3StudCharacterMessage");
        return jsonObject.toString();
    }

    public static String serialize(SimilMotherNatureMesage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","SimilMotherNatureMessage");
        return jsonObject.toString();
    }

    public static String serialize(StrategyProfessorMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","StrategyProfessorMessage");
        return jsonObject.toString();
    }

    public static String serialize(PlayerMessage message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","PlayerMessage");
        return jsonObject.toString();
    }
}
