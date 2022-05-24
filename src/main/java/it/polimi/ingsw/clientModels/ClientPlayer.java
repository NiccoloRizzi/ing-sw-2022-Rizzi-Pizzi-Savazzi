package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.ModelSerializer;

public class ClientPlayer implements ClientModel{

    private final Integer[] usedAssistants;
    private final Integer[] deck;
    private final int coins;
    private final String nickname;
    private final int id;
    private final boolean boost;

    public Integer[] getUsedAssistants() {
        return usedAssistants;
    }

    public Integer[] getDeck() {
        return deck;
    }

    public int getCoins() {
        return coins;
    }

    public String getNickname() {
        return nickname;
    }

    public int getId() {
        return id;
    }

    public boolean isBoost() {
        return boost;
    }

    public ClientPlayer(Integer[] usedAssistants, Integer[] deck, int coins, String nickname, int id, boolean boost)
    {
        this.usedAssistants = usedAssistants;
        this.deck = deck;
        this.coins = coins;
        this.nickname = nickname;
        this.id = id;
        this.boost = boost;
    }
    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }

    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientPlayer that = (ClientPlayer) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
