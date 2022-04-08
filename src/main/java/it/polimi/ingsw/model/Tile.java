package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Tile
{
    protected HashMap<Colour, Integer> students;

    public Tile ()
    {
        students = new HashMap<>();
        for(Colour c: Colour.values()){
            students.put(c,0);
        }
    }
    public boolean isEmpty(){
        int sum=0;
        for(Colour c: Colour.values()){
            sum+= students.get(c);
        }
        return sum<=0;
    }

    public void addStudent(Colour col)
    {
        students.replace(col, students.get(col)+1);
    }

    public void addStudents(HashMap<Colour, Integer> studentsToAdd){
        for(Colour c : Colour.values()){
            students.replace(c, students.get(c) + 1);
        }
    }
}