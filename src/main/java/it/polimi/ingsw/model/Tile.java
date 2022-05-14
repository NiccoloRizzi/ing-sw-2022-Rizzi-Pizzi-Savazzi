package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observable;

import java.util.HashMap;

public abstract class Tile extends Observable<ClientModel>
{
    protected int ID;
    protected HashMap<Colour, Integer> students;

    public Tile (int ID)
    {
        this.ID = ID;
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
        notifyChange();
    }

    public void addStudents(HashMap<Colour, Integer> studentsToAdd){
        for(Colour c : Colour.values()){
            students.replace(c, students.get(c)+studentsToAdd.get(c));
        }
        notifyChange();
    }

    public abstract void notifyChange ();
    public int getStudents(Colour c){
        return students.get(c);
    }

    public int getID()
    {
        return ID;
    }
}