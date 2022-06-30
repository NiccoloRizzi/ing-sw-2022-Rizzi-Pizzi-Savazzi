package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientBoard;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TowerOutOfBoundException;

import java.util.Arrays;
import java.util.HashMap;
/**
 * Class that represents a player's board
 */
public class Board extends Tile{


    /**
     * The board's towers faction
     */
    private Faction faction;
    /**
     * The number of towers available
     */
    private int towers;
    /**
     *  The maximum number of towers the board can hold
     */
    private final int towersLimit;
    /**
     * The tables of the board
     */
    private final HashMap<Colour, Integer> tables;
    /**
     * The maximum number of students the board can hold
     */
    private final int studLimit;

    /**
     * Constructor for the board.
     *
     * @param faction is the player faction (Black, White, Grey).
     * @param towers is the number of starting towers.
     */
    public Board(Faction faction, int towers,int playerID) {
        super(playerID);
        this.faction = faction;
        this.towers = towers;
        this.studLimit = (towers == 6)?9:7;
        this.towersLimit = towers;
        tables = new HashMap<>();
        for(Colour c: Colour.values()){
            tables.put(c,0);
        }
    }

    /**
     * Removes a student from the entrance.
     * This function also notify the changes to the observer of the model [See notifyChange()].
     *
     * @param c is the colour of the student to be removed.
     * @throws StudentsOutOfBoundsException if there are no student of that color.
     */
    public void removeStudent(Colour c) throws StudentsOutOfBoundsException {
        if(students.get(c)>0){
            students.replace(c,students.get(c)-1);
            notifyChange();
        }
        else
            throw new StudentsOutOfBoundsException();
    }



    /**
     * Add a student to the board entrance.
     * This function also notify the changes to the observer of the model [See notifyChange()].

     * @param student is colour of the student that will be added to the player's entrance.
     * @throws StudentsOutOfBoundsException when the entrance is full.
     */
    public void addToEntrance(Colour student) throws StudentsOutOfBoundsException {
        if(!isEntranceFull()){
            students.replace(student,students.get(student)+1);
            notifyChange();
        }
        else
            throw new StudentsOutOfBoundsException();

    }

    /**
     * Get the number of student form the table of the specified color.
     *
     * @param c is the colour of the table.
     * @return the number of students in the table of the given colour.
     */
    public int getTable(Colour c) {
        return tables.get(c);
    }

    /**
     * Add a student to the table of the specified color.
     * This function also notify the changes to the observer of the model [See notifyChange()].

     * @param table The position of the table in the collection.
     * @throws StudentsOutOfBoundsException if the table is full.
     */
    public void addToTable (Colour table) throws StudentsOutOfBoundsException{
        if(!isTableFull(table)){
            tables.replace(table,tables.get(table)+1);
            notifyChange();
        }
        else
            throw new StudentsOutOfBoundsException();
    }

    /**
     * Add the specified number of tower to the board.
     * This function also notify the changes to the observer of the model [See notifyChange()].

     * @param t is the number of towers to add.
     * @throws TowerOutOfBoundException if the tower number is above the game limit.
     */
    public void addTowers(int t) throws TowerOutOfBoundException{
        if(towers+t<=towersLimit){
            towers+=t;
            notifyChange();
        }else{
            throw new TowerOutOfBoundException();
        }
    }


    /**
     * Remove the specified number of tower from the board.
     * This function also notify the changes to the observer of the model [See notifyChange()].
     *
     * @param t is the umber of tower to remove.
     * @throws TowerOutOfBoundException if the removed towers are more than the actual present.
     */
    public void useTowers(int t) throws TowerOutOfBoundException{
        if(towers>=t){
            towers-=t;
            notifyChange();
        }else{
            throw new TowerOutOfBoundException();
        }
    }

    /**
     * Return true if the table of the specified color is full.
     *
     * @param table is the colour of the table.
     * @return true if the table of the specified color is full.
     */
    public boolean isTableFull(Colour table){
        return (tables.get(table)>=10);
    }

    /**
     * Return true if the entrance is full.
     *
     * @return true if the entrance is full.
     */
    public boolean isEntranceFull(){
       int students= (Arrays.stream(Colour.values()))
                .map(C -> super.students.get(C))
                .reduce(0, Integer::sum);

       return students>=studLimit;
    }

    /**
     * Set the faction of the board.
     * This function also notify the changes to the observer of the model [See notifyChange()].

     * @param faction is the faction to set to the board.
     */
    public void setFaction(Faction faction){
        this.faction = faction;
        notifyChange();
    }

    /**
     * Return the faction of the board.
     *
     * @return the faction of the board.
     */
    public Faction getFaction() {
        return faction;
    }

    /**
     * Return the number of towers.
     *
     * @return the number of towers.
     */
    public int getTowers() {
        return towers;
    }

    /**
     * Remove a student from the specified table.
     * This function also notify the changes to the observer of the model [See notifyChange()].

     * @param table is the colour of the table.
     * @throws StudentsOutOfBoundsException if there are no student to remove.
     */
    public void removeFromTable(Colour table) throws StudentsOutOfBoundsException{
        if(tables.get(table)>0){
            tables.replace(table,tables.get(table)-1);
            notifyChange();
        }
        else{
            throw new StudentsOutOfBoundsException();
        }
    }

    /**
     * @param table is the colur of the table.
     * @return true if the student's number in the specified table have reached the coin spot.
     */
    public boolean checkCoin(Colour table)
    {
        return (tables.get(table)>0 && (tables.get(table)%3==0));
    }

    /**
     * Notify the model observer (PLayerConnection) all the changes constructing a ClientBoard message.
     */
   @Override
    public void notifyChange(){
        HashMap<Colour,Integer> tempTables = new HashMap<>(tables);
        HashMap<Colour,Integer> tempStudents = new HashMap<>(students);
        notify(new ClientBoard(ID,faction,towers,tempTables,tempStudents));
    }
}
