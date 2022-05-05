package it.polimi.ingsw.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.client.ClientBoard;
import it.polimi.ingsw.messages.*;

public class MoveSerializer {

    private static final Gson gson = new Gson();

    public static Message deserialize(String json){
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        String type = jsonObject.get("type").getAsString();
        switch(type){
            case "AssistantChoiceMessage":
                return gson.fromJson(json,AssistantChoiceMessage.class);
            case "CloudChoiceMessage":
                return gson.fromJson(json,CloudChoiceMessage.class);
            case "IsleInfluenceCharacterMessage":
                return gson.fromJson(json,IsleInfluenceCharacterMessage.class);
            case "Move2StudCharacterMessage":
                return gson.fromJson(json,Move2StudCharacterMessage.class);
            case "Move6StudCharacterMessage":
                return gson.fromJson(json,Move6StudCharacterMessage.class);
            case "MoveMotherNatureMessage":
                return gson.fromJson(json,MoveMotherNatureMessage.class);
            case "MoveStudentCharacterMessage":
                return gson.fromJson(json,MoveStudentCharacterMessage.class);
            case "MoveStudentMessage":
                return gson.fromJson(json,MoveStudentMessage.class);
            case "Plus2MoveMnMessage":
                return gson.fromJson(json,Plus2MoveMnMessage.class);
            case "ProhibitedIsleCharacterMessage":
                return gson.fromJson(json,ProhibitedIsleCharacterMessage.class);
            case "Remove3StudCharacterMessage":
                return gson.fromJson(json,Remove3StudCharacterMessage.class);
            case "SimilMotherNatureMessage":
                return gson.fromJson(json,SimilMotherNatureMesage.class);
            case "StrategyProfessorMessage":
                return gson.fromJson(json,StrategyProfessorMessage.class);
            default:
                return null;
        }
    }

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

    public static String serialize(ClientBoard message){
        String json = gson.toJson(message);
        JsonObject jsonObject = gson.fromJson(json,JsonObject.class);
        jsonObject.addProperty("type","ClientBoard");
        return jsonObject.toString();
    }

}
