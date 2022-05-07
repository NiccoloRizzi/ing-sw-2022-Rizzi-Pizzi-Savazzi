package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Cloud extends Tile {

    public Cloud(int ID) {
        super(ID);
    }

    public HashMap<Colour,Integer> empty() {
        HashMap<Colour, Integer> temp = new HashMap<>(students);
        for(Colour c:Colour.values())
            students.put(c,0);
        return temp;
    }
}