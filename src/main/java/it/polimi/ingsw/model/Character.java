package it.polimi.ingsw.model;

public class Character {
    private final CharactersEnum card;
    private int price;
    private boolean used = false;

    public Character(CharactersEnum character)
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
