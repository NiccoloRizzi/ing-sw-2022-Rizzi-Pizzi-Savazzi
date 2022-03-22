package it.polimi.ingsw.model;

public abstract class Character {

    private int price;
    private final int id;
    private boolean used = false;

    public Character(int id, int price)
    {
        this.id = id;
        this.price = price;
    }

    public int getPrice(){
        return price;
    }

    public int getId(){
        return id;
    }
    
    public void use(){
        if(!used){
            price++;
            used=true;
        }
    }


}
