package it.polimi.ingsw.model;

/*UML Changes
* 1) Add Faction attribute
* 2) Add N_ASSISTANT attribute
* 3) setChoosenAssistant to add out_of_range_exception
* 4) Add createBoard()
* 5) Add getID()*/

import java.util.ArrayList;

public class Player {

    private static final int N_ASSISTANT = 10;

    private int ID;
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

    public void setChoosenAssistant(int i){
        if(i>= 0 && i <= 10){
            usedCards.add(deck.remove(i)); // Maybe i had to add some controll on the remove...XD
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
        return usedCards;
    }

    // Utile on no??? Va tolto dal costruttore???
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }


    public int getID() {
        return ID;
    }

    public void createBoard(int nTowers){
        school = new Board(this.faction, nTowers);
    }
}
