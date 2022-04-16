package it.polimi.ingsw.model;

import java.util.ArrayList;

/*UML Changes
 * 1) Added addPlayer() */

// ATTENTION: TEAM IMPLEMENTATION NEED TO BE DEFINED CLEARLY
public class Team {

    private final static int MAX_N_PLAYERS = 2;

    private Faction faction;
    private int towers;
    private final ArrayList<Player> players;

    public Team(){
        this.faction = Faction.Empty;
        this.towers = 8;
        this.players = new ArrayList<>();
        // this.players.add(new Player(0, null, 0)); // ATTENTION
        // this.players.add(new Player(1, null, 0)); // ATTENTION
    }

    /**
     *
     * @return the leader of the team (player in index 0 of the player array)
     */
    public Player getLeader(){
        return players.get(0); // ATTENTION
    }

    /**
     *
     * @return the player who's not the leader (player in index 1 of the player array)
     */
    public Player getMember(){
        return players.get(1); // ATTENTION
    }

    public void useTowers(int t){
        if(towers>=t) {
            towers-=t;
            players.get(0).getBoard().useTowers(t);
            players.get(1).getBoard().useTowers(t);
        }
    }

    public void addTowers(int t){
        if(towers+t<=8){
            towers+=t;
            players.get(0).getBoard().addTowers(t);
            players.get(1).getBoard().addTowers(t);
        }
    }

    /**
     *
     * @return faction of the team
     */
    public Faction getFaction() {
        return faction;
    }

    /**
     *
     * @param faction to assign to the team and both players
     */
    public void assignFaction(Faction faction){
        this.faction = faction;
        getLeader().assignFaction(faction);
        getMember().assignFaction(faction);
    }

    /**
     *
     * @param p to add to the team
     * @throws IndexOutOfBoundsException when players exceed the limit of 2
     */
    public void addPlayer(Player p){
        if(this.players.size() < MAX_N_PLAYERS) {
            this.players.add(p);
        }else{
            throw new IndexOutOfBoundsException();
        }
    }
}
