package it.polimi.ingsw.model;

import java.util.ArrayList;

/*UML Changes
 * 1) Added addPlayer() */

// ATTENTION: TEAM IMPLEMENTATION NEED TO BE DEFINED CLEARLY
public class Team {

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
    }

    public void addPlayer(Player p){
        this.players.add(p);
    }
}
