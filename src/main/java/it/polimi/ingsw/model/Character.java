package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observable;

public class Character extends Observable<ClientModel> {
    protected int ID;
    protected final CharactersEnum card;
    protected int price;
    protected boolean used = false;

    public Character(int ID,CharactersEnum character)
    {
        this.ID = ID;
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
            notifyChange();
        }
    }

    public boolean getUsed()
    {
        return used;
    }

    public void notifyChange()
    {
        notify(new ClientCharacter(ID,card,price));
    }

}
