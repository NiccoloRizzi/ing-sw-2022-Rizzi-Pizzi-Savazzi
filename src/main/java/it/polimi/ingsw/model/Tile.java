package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.Observable;

import java.util.HashMap;

/**
 * Abstract class that represents a generic tile of the game
 */
public abstract class Tile extends Observable<ClientModel>
{
    protected final int ID;
    /**
     * The students the tile is holding
     */
    protected final HashMap<Colour, Integer> students;

    /**
     * Creates a tile
     * @param ID the id of the tile
     */
    public Tile (int ID)
    {
        this.ID = ID;
        students = new HashMap<>();
        for(Colour c: Colour.values()){
            students.put(c,0);
        }
    }

    /**
     *
     * @return whether the tile is empty and has no students
     */
    public boolean isEmpty(){
        int sum=0;
        for(Colour c: Colour.values()){
            sum+= students.get(c);
        }
        return sum<=0;
    }

    /**
     * Adds a student to the tile
     * @param col the colour of the student to add
     */
    public void addStudent(Colour col)
    {
        students.replace(col, students.get(col)+1);
        notifyChange();
    }

    /**
     * Adds multiple students to the tile
     * @param studentsToAdd The hashmap containing the students to add
     */
    public void addStudents(HashMap<Colour, Integer> studentsToAdd){
        for(Colour c : Colour.values()){
            students.replace(c, students.get(c)+studentsToAdd.get(c));
        }
        notifyChange();
    }

    /**Notify changes**/
    public abstract void notifyChange ();

    /**Get the number of students in the tile**/
    public int getStudents(Colour c){
        return students.get(c);
    }

    /**
     *
     * @return the id of the tile
     */
    public int getID()
    {
        return ID;
    }
}