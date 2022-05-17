package it.polimi.ingsw.model;

/*UML Changes
* 3) setChoosenAssistant to add out_of_range_exception
*/

import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.clientModels.ClientPlayer;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

import java.util.ArrayList;
import java.util.Objects;

public class Player extends Observable<ClientModel> {

    private static final int N_ASSISTANT = 10;

    private final int ID;
    private String nickname;
    private Faction faction;
    private int coins;
    private Board school;
    private final ArrayList<Assistant> deck;
    private final ArrayList<Assistant> usedCards;

    /**@param ID of the player
     * @param nickname of the player**/
    public Player(int ID, String nickname){
        this.ID = ID;
        this.nickname = nickname;
        this.faction = Faction.Empty;
        coins = 0;
        deck = new ArrayList<>();
        for(int i = 0; i < N_ASSISTANT; i++){
            deck.add(new Assistant(i+1, (i+2)/2));
        }
        usedCards = new ArrayList<>();
    }

    /**@return choosen assistant
     * @throws IndexOutOfBoundsException if usedCards is empty**/
    public Assistant getChosen() throws IndexOutOfBoundsException{
        return usedCards.get(usedCards.size() - 1);
    }

    /**@return deck of assistants of the player**/
    public ArrayList<Assistant> getDeck(){
        return deck;
    }

    /**@param i is the index of the array of the assistants
     * @throws IndexOutOfBoundsException if i is not a valid value**/
    public void setChoosenAssistant(int i) throws IndexOutOfBoundsException{
        usedCards.add(deck.remove(i)); // Maybe i had to add some controll on the remove...XD
        notifyChange();
    }

    /**@return the board of the player**/
    public Board getBoard(){
        return school;
    }

    /**
     * Add a single coin
     */
    public void addCoin(){
        coins++;
        notifyChange();
    }

    /**
     * @param faction to assign to the player and the board
     */
    public void assignFaction(Faction faction){
        this.faction = faction;
        school.setFaction(faction);
    }

    /**
     * @return used assistants
     */
    public ArrayList<Assistant> getUsedCards(){
        return usedCards;
    }

    /**
     *
     * @param nickname to give to the player
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     *
     * @return player's nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param o is the player
     * @return true if the players ID are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return ID == player.ID;
    }

    /**
     *
     * @return the hash code based in ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    /**
     *
     * @return id of the player
     */
    public int getID() {
        return ID;
    }

    /**
     *
     * @return player's coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     *
     * @param nTowers is the number of towers the board need based on the number of players
     */
    public void createBoard(int nTowers){
        school = new Board(this.faction, nTowers,ID);
    }

    public boolean hasUsed(int assistantId){
        for(Assistant a: usedCards){
            if(a.getValue()==assistantId+1)
                return true;
        }
        return false;
    }

    public void removeCoins(int coins)
    {
        this.coins = this.coins-coins;
        notifyChange();
    }

    public void boost()
    {
        getChosen().Boost();
        notifyChange();
    }
    public void notifyChange()
    {
        Integer[] used = usedCards.stream().map(x->x.getValue()).toArray(Integer[]::new);
        Integer[] intDeck = deck.stream().map(x->x.getValue()).toArray(Integer[]::new);
        notify(new ClientPlayer(used,intDeck,coins,nickname,ID));
    }

    @Override
    public void addObserver(Observer<ClientModel> observer) {
        super.addObserver(observer);
        school.addObserver(observer);
    }
}
