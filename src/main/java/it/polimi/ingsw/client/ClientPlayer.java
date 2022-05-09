package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Faction;

import java.util.ArrayList;

public class ClientPlayer implements ClientModel{

    private Integer[] usedAssistants;
    private Integer[] deck;
    private int coins;
    private String nickname;
    private int id;

    public ClientPlayer(Integer[] usedAssistants,Integer[] deck,int coins,String nickname,int id)
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
}
