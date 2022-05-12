package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class StartMessage {
    int playerNumbers;
    HashMap<String, Tuple> players;

    public StartMessage(ArrayList<Player> players) {
        this.players = new HashMap<>();
        for(Player p: players){
            this.players.put(p.getNickname(), new Tuple(p.getID(), p.getBoard().getFaction()));
        }
    }

    public Tuple getPlayer(String nickname){
        return players.get(nickname);
    }


    void accept(View view){

    }

    private class Tuple{
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
