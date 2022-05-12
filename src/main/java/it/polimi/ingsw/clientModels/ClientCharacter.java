package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;

import java.util.HashMap;

public class ClientCharacter implements ClientModel{

    int ID;
    private final CharactersEnum card;
    private int price;
    private final HashMap<Colour, Integer> students;

    public ClientCharacter(int ID, CharactersEnum card, int price)
    {
        this.ID = ID;
        this.card = card;
        this.price = price;
        this.students = null;
    }

    public ClientCharacter(int ID, CharactersEnum card, int price,HashMap<Colour, Integer> students)
    {
        this.ID = ID;
        this.card = card;
        this.price = price;
        this.students = students;
    }

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}
