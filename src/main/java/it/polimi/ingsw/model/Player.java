package it.polimi.ingsw.model;

/*UML Changes
* 3) setChoosenAssistant to add out_of_range_exception
*/

import java.util.ArrayList;
import java.util.Objects;

public class Player {

    private static final int N_ASSISTANT = 10;

    private final int ID;
    private String nickname;
    private Faction faction;
    private int coins;
    private Board school;
    private final ArrayList<Assistant> deck;
    private final ArrayList<Assistant> usedCards;

    // Param nickname??? see setNickname() comment
    public Player(int ID, String nickname){
        this.ID = ID;
        this.nickname = nickname;
        this.faction = Faction.Empty;
        coins = 0;
        deck = new ArrayList<>();
        for(int i = 0; i < N_ASSISTANT; i++){
            deck.add(new Assistant(i + 1, (i+2)/2));
        }
        usedCards = new ArrayList<>();
    }

    // NULL if usedCards is empty
    public Assistant getChosen(){
        if(!usedCards.isEmpty()){
            return usedCards.get(usedCards.size() - 1);
        }
        return null;
    }

    // Clone or not clone???
    public ArrayList<Assistant> getDeck(){
        return deck;
    }

    // REFERS TO ASSISTANT ARRAY POSTIION
    public void setChoosenAssistant(int i){
        usedCards.add(deck.remove(i)); // Maybe i had to add some controll on the remove...XD
    }

    public Board getBoard(){
        return school;
    }

    public void addCoin(){
        coins++;
    }

    public void assignFaction(Faction faction){
        this.faction = faction;
        school.setFaction(faction);
    }

    // Clone or not clone???
    public ArrayList<Assistant> getUsedCards(){
        return usedCards;
    }

    // Utile on no??? Va tolto dal costruttore???
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return ID == player.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    public int getID() {
        return ID;
    }

    public int getCoins() {
        return coins;
    }

    public void createBoard(int nTowers){
        school = new Board(this.faction, nTowers);
    }
}
