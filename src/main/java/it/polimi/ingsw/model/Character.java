package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observable;
/**
 * Class that represents usable characters
 */
public class Character extends Observable<ClientModel> {


    /**
     * Unique ID of the character
     */
    protected int ID;
    /**
     * The type of character
     */
    protected final CharactersEnum card;
    /**
     * The price of the character
     */
    protected int price;
    /**
     * Whether the character has been already used
     */
    protected boolean used = false;

    /**
     * Constructor of a character object
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
     * Increments the character price and sets it as used.
     * This function also notifies the changes to the observer of the model [See notifyChange()].
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
