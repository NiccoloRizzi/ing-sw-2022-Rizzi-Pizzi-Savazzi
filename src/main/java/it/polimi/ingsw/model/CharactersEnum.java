package it.polimi.ingsw.model;

public enum CharactersEnum {
    ONE_STUD_TO_ISLE(1),
    PROFESSOR_CONTROL(2),
    SIMIL_MN(3),
    PLUS_2_MN(1),
    PROHIBITED(2),
    NO_TOWER_INFLUENCE(3),
    EXCHANGE_3_STUD(1),
    EXCHANGE_2_STUD(2),
    PLUS_2_INFLUENCE(2),
    ONE_STUD_TO_TABLES(2),
    REMOVE_3_STUD(2),
    NO_COLOUR_INFLUENCE(3);

    private final int price;

     CharactersEnum(int price){
         this.price= price;
    }

    public int getPrice() {
        return price;
    }
}
