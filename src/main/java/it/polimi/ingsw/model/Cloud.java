package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Cloud extends Tile {

    public Cloud() {}
    public ArrayList<Student> empty() {
        ArrayList<Student> temp = new ArrayList<Student>(students);
        students.clear();
        return students;
    }
}