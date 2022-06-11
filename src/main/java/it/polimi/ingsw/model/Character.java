package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observable;

public class Character extends Observable<ClientModel> {
    protected int ID;
    protected final CharactersEnum card;
    protected int price;
    protected boolean used = false;

    /**
     * @param ID is the character position in the active characters array.
     * @param character is the character type from CharacterEnum.
     */
    public Character(int ID,CharactersEnum character)
    {
        this.ID = ID;
        this.card = character;
        this.price = character.getPrice();
    }

    /**
     * @return the current character price.
     */
    public int getPrice(){
        return price;
    }

    /**
     * @return the type of the character.
     */
    public CharactersEnum getCard(){
        return card;
    }

    /**
     * Increment the character price and set it used.
     * This function also notify the changes to the observer of the model [See notifyChange()].
     */
    public void use(){
        if(!used){
            price++;
            used=true;
        }
        System.out.println(card);
        notifyChange();
    }

    /**
     * @return true if the character was used.
     */
    public boolean getUsed()
    {
        return used;
    }

    /**
     * Notify the model observer (PLayerConnection) all the changes constructing a ClientCharacter message.
     */
    public void notifyChange()
    {
        notify(new ClientCharacter(ID,card,price));
    }

}
