package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.ArrayList;
import java.util.HashMap;

public class StartMessage implements ClientModel {
    int playerNumbers;
    HashMap<String, Integer> players;

    public StartMessage(ArrayList<Player> players) {
        this.playerNumbers = players.size();
        this.players = new HashMap<>();
        for(Player p: players){
            this.players.put(p.getNickname(),p.getID());
        }
    }

    public int getId(String nickname){
        return players.get(nickname);
    }

    public int getPlayerNumbers() {
        return playerNumbers;
    }

    public void accept(View view){
        view.visit(this);
    }

    @Override
    public String serialize() {
        return ModelSerializer.serialize(this);
    }

}
