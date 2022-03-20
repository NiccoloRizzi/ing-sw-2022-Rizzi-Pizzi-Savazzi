package it.polimi.ingsw.model;

import java.util.ArrayList;

// ATTENTION: TEAM IMPLEMENTATION NEED TO BE DEFINED CLEARLY
public class Team {

    private int points;
    private Faction faction;
    private int towers;
    private ArrayList<Player> players;

    public Team(){
        this.points = 0;
        this.faction = Faction.Empty;
        this.towers = 8;
        this.players = new ArrayList<>();
        this.players.add(new Player(0, null, 0)); // ATTENTION
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

    public int getPoints() {
        return points;
    }

    public void assignFaction(Faction faction){
        this.faction = faction;
    }
}
