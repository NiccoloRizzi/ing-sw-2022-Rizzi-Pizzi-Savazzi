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
    HashMap<String, Tuple> players;

    public StartMessage(ArrayList<Player> players) {
        this.playerNumbers = players.size();
        this.players = new HashMap<>();
        for(Player p: players){
            this.players.put(p.getNickname(), new Tuple(p.getID(), p.getBoard().getFaction()));
        }
    }

    public Tuple getPlayer(String nickname){
        return players.get(nickname);
    }

    public int getPlayerNumbers() {
        return playerNumbers;
    }

//    public HashMap<String, Tuple> getPlayers() {
//        return players;
//    }

    public void accept(View view){
        view.visit(this);
    }

    @Override
    public String serialize() {
        return ModelSerializer.serialize(this);
    }

    public class Tuple{
        int id;
        Faction faction;

        public Tuple(int id, Faction faction) {
            this.id = id;
            this.faction = faction;
        }

        public int getId() {
            return id;
        }

        public Faction getFaction() {
            return faction;
        }
    }
}
