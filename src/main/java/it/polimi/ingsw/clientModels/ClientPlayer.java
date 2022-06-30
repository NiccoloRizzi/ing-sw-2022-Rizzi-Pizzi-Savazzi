package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.ModelSerializer;

/**
 * Client-side version of Player class containing information of interest to the players and that can be sent as an update
 */
public class ClientPlayer implements ClientModel{
    /**
     * IDs of the used assistants
     */
    private final Integer[] usedAssistants;
    /**
     * IDs of the usable assistants in the deck
     */
    private final Integer[] deck;
    /**
     * Amout of coins owned by the player
     */
    private final int coins;
    /**
     * Nickname of the player
     */
    private final String nickname;
    /**
     * ID of the player
     */
    private final int id;
    /**
     * Whether the player has used a character to boost his assistants
     */
    private final boolean boost;

    /**
     * Getter for used Assistants
     * @return Used Assistants' IDs
     */
    public Integer[] getUsedAssistants() {
        return usedAssistants;
    }

    /**
     * Getter for deck of assistants
     * @return Deck of assistants
     */
    public Integer[] getDeck() {
        return deck;
    }

    /**
     * Getter for player's coins
     * @return Number of player's coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Getter for player's Nickname
     * @return Player's Nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Getter for player's ID
     * @return Player's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns whether the player has used a character to boost his assistants
     * @return Whether the player has used a character to boost his assistants
     */
    public boolean isBoost() {
        return boost;
    }

    /**
     * Constructor for ClientPlayer
     * @param usedAssistants IDs of used assistants
     * @param deck IDs of usable assistants in deck
     * @param coins Number of player's coin
     * @param nickname Nickname of the player
     * @param id ID of the player
     * @param boost Whether the player has boosted his assistants
     */
    public ClientPlayer(Integer[] usedAssistants, Integer[] deck, int coins, String nickname, int id, boolean boost)
    {
        this.usedAssistants = usedAssistants;
        this.deck = deck;
        this.coins = coins;
        this.nickname = nickname;
        this.id = id;
        this.boost = boost;
    }
    /**
     * Accept method for visitor pattern
     * @param visitor Client-view, acting as a Visitor for server updates
     */
    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }

    /**
     * Equals for ClientPlayer
     * @param o Object compared to the player
     * @return Whether it's an equivalent object
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientPlayer that = (ClientPlayer) o;

        return id == that.id;
    }

    /**
     * HashCode for ClientPlayer
     * @return Hash of the ClientPlayer
     */
    @Override
    public int hashCode() {
        return id;
    }
}
