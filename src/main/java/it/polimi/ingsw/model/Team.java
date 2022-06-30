package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TowerOutOfBoundException;
import java.util.ArrayList;


/**
 * Class that represents teams in the game
 */
public class Team {

    private final static int MAX_N_PLAYERS = 2;

    private Faction faction;
    private int towers;
    /**
     * The players in the team
     */
    private final ArrayList<Player> players;

    public Team(){
        this.faction = Faction.Empty;
        this.towers = 8;
        this.players = new ArrayList<>();
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

    /**
     * Removes towers from all the players in the team
     * @param t The team from which towers will be removed
     * @throws TowerOutOfBoundException When using more towers than the player has
     */
    public void useTowers(int t) throws TowerOutOfBoundException{
        if(towers>=t) {
            towers-=t;
            players.get(0).getBoard().useTowers(t);
            players.get(1).getBoard().useTowers(t);
        }
    }

    /**
     * Add towers to all the players in the team
     * @param t the team to which towers will be added
     * @throws TowerOutOfBoundException When adding more towers than the players' boards can hold
     */
    public void addTowers(int t) throws TowerOutOfBoundException {
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
