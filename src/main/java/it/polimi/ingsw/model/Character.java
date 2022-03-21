package it.polimi.ingsw.model;

public abstract class Character {

    private int price;
    private boolean used = false;

    public Character(int price){
        this.price = price;
    }

    public int getPrice(){
        if(used)
            return price+1;
        return price;
    }

    public void use(){
        used=true;
    }

    public void addPrice(){
        price++;
    }

}
