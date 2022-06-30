package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Message sent to the player to notify the start of the match
 */
public class StartMessage implements ClientModel {
    /**
     * Number of players in the match
     */
    int playerNumbers;
    /**
     * HashMap associating players Nicknames to IDs
     */
    HashMap<String, Integer> players;

    /**
     * StartMessage constructor
     * @param players List of players in the match
     */
    public StartMessage(ArrayList<Player> players) {
        this.playerNumbers = players.size();
        this.players = new HashMap<>();
        for(Player p: players){
            this.players.put(p.getNickname(),p.getID());
        }
    }

    /**
     * Getter for player's Id, given his nickname
     * @param nickname Nickname of hte player
     * @return Player's Id associated to the given Nickname
     */
    public int getId(String nickname){
        return players.get(nickname);
    }

    /**
     * Accept method for visitor pattern
     * @param view Client-Side visitor for server updates
     */
    @Override
    public void accept(View view){
        view.visit(this);
    }

    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    @Override
    public String serialize() {
        return ModelSerializer.serialize(this);
    }

}
