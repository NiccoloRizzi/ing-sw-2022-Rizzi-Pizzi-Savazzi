package it.polimi.ingsw.model;

import java.util.ArrayList;

/*UML Changes
 * 1) Added addPlayer() */

// ATTENTION: TEAM IMPLEMENTATION NEED TO BE DEFINED CLEARLY
public class Team {

    private final static int MAX_N_PLAYERS = 2;

    private Faction faction;
    private int towers;
    private ArrayList<Player> players;

    public Team(){
        this.faction = Faction.Empty;
        this.towers = 8;
        this.players = new ArrayList<>();
        // this.players.add(new Player(0, null, 0)); // ATTENTION
        // this.players.add(new Player(1, null, 0)); // ATTENTION
    }

    public Player getLeader(){
        return players.get(0); // ATTENTION
    }

    public Player getMember(){
        return players.get(1); // ATTENTION
    }

    public Faction getFaction() {
        return faction;
    }

    public void assignFaction(Faction faction){
        this.faction = faction;
        getLeader().assignFaction(faction);
        getMember().assignFaction(faction);
    }

    public boolean addPlayer(Player p){
        if(this.players.size() < MAX_N_PLAYERS) {
            this.players.add(p);
            return true;
        }else{
            return false;
        }
    }
}
