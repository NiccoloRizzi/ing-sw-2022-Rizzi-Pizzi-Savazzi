package it.polimi.ingsw.model;

/*UML Changes
* 1) Add Faction attribute
* 2) Add N_ASSISTANT attribute
* 3) setChoosenAssistant to add out_of_range_exception
* 4) */

import java.util.ArrayList;

public class Player {

    private static final int N_ASSISTANT = 10;

    private int ID;
    private String nickname;
    private Faction faction;
    private int points;
    private int coins;
    private Board school;
    private ArrayList<Assistant> deck;
    private ArrayList<Assistant> usedCards;

    // Param nickname??? see setNickname() comment
    public Player(int ID, String nickname, int nTowers){
        this.ID = ID;
        this.nickname = nickname;
        this.faction = Faction.Empty;
        points = 0;
        coins = 0;
        school = new Board(this.faction, nTowers);
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
        return (ArrayList<Assistant>) deck.clone();
    }

    public void setChoosenAssistant(int i){
        if(i>= 0 && i <= 10){
            usedCards.add(deck.remove(i));
        }else{
            System.out.println("Player.setChoosenAssistant(int i), i out of range");
        }
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
        return (ArrayList<Assistant>) usedCards.clone();
    }

    // Utile on no??? Va tolto dal costruttore???
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
