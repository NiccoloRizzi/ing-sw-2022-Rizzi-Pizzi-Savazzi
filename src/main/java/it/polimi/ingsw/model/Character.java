package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observable;

public class Character extends Observable<ClientModel> {
    int ID;
    private final CharactersEnum card;
    private int price;
    private boolean used = false;

    public Character(int ID,CharactersEnum character)
    {
        this.card = character;
        this.price = character.getPrice();
    }

    public int getPrice(){
        return price;
    }

    public CharactersEnum getCard(){
        return card;
    }

    public void use(){
        if(!used){
            price++;
            used=true;
        }
    }

    public boolean getUsed()
    {
        return used;
    }

}
