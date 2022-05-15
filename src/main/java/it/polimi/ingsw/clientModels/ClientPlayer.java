package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.server.ModelSerializer;

public class ClientPlayer implements ClientModel{

    private final Integer[] usedAssistants;
    private final Integer[] deck;
    private final int coins;
    private final String nickname;
    private final int id;

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

    public ClientPlayer(Integer[] usedAssistants, Integer[] deck, int coins, String nickname, int id)
    {
        this.usedAssistants = usedAssistants;
        this.deck = deck;
        this.coins = coins;
        this.nickname = nickname;
        this.id = id;
    }
    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }

    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }
}
