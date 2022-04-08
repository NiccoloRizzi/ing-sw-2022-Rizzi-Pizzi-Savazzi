package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Cloud extends Tile {

    public Cloud() {
        super();
    }

    public HashMap<Colour,Integer> empty() {
        HashMap<Colour, Integer> temp = new HashMap<>(students);
        students.forEach((k, v) -> v = 0);
        return temp;
    }
}