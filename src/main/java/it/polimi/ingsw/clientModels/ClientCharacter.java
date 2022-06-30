package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.HashMap;

/**
 * Client-side version of the character class containing information of interest to the players and that can be sent as an update
 */
public class ClientCharacter implements ClientModel{
    /**
     * The ID (position on the board) of the character
     */
    final int ID;
    /**
     * The type of character
     */
    private final CharactersEnum card;
    /**
     * The current use price of the character
     */
    private final int price;
    /**
     * The students currently placed on the character
     */
    private final HashMap<Colour, Integer> students;

    /**
     * Constructor for clientCharacter in case the character may not contain students
     * @param ID The ID (position on the board) of the character
     * @param card The type of character
     * @param price The current use price of the character
     */
    public ClientCharacter(int ID, CharactersEnum card, int price)
    {
        this.ID = ID;
        this.card = card;
        this.price = price;
        this.students = null;
    }
    /**
     * Constructor for clientCharacter in case the character may contain students
     * @param ID The ID (position on the board) of the character
     * @param card The type of character
     * @param price The current use price of the character
     * @param students Students currently placed on the character
     */
    public ClientCharacter(int ID, CharactersEnum card, int price,HashMap<Colour, Integer> students)
    {
        this.ID = ID;
        this.card = card;
        this.price = price;
        this.students = students;
    }

    /**
     * Getter for character's ID
     * @return Character's ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Getter for character's type
     * @return Character's type
     */
    public CharactersEnum getCard() {
        return card;
    }

    /**
     * Getter for character's current price
     * @return Character's current price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Getter for students placed on the character
     * @return Students placed on the character
     */
    public HashMap<Colour, Integer> getStudents() {
        return students;
    }
    /**
     * Accept method for visitor pattern
     * @param visitor Client-view, acting as a Visitor for server updates
     */
    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }
}
