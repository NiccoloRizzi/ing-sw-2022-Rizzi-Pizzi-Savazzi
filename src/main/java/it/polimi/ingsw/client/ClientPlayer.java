package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Faction;

import java.util.ArrayList;

public class ClientPlayer implements ClientModel{

    private int[] usedAssistants;
    private int[] deck;
    private int chosenAssistant;
    private int coins;
    private String nickname;
    private int id;

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}
