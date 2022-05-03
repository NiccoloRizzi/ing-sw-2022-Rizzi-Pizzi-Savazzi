package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;
import it.polimi.ingsw.exceptions.TowerOutOfBoundException;

import java.util.Arrays;
import java.util.HashMap;

public class Board extends Tile{
    private Faction faction;
    private int towers;
    private final int towersLimit;
    private final HashMap<Colour, Integer> tables;
    private final int studLimit;

    /**
     * Constructor for the board.
    * @param faction The colour of the towers (Black, White, Grey)
     * @param towers The number of starting towers
     */
    public Board(Faction faction, int towers) {
        super();
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
     * @param c Colour of the students to be removed
     */
    public void removeStudent(Colour c) throws StudentsOutOfBoundsException {
        if(students.get(c)>0){
            students.replace(c,students.get(c)-1);
        }
        else
            throw new StudentsOutOfBoundsException();
    }



    /**
     *
     * @param student Colour of the student that will be added to the player's entrance
     * @throws StudentsOutOfBoundsException When adding a student to a full entrance
     */
    public void addToEntrance(Colour student) throws StudentsOutOfBoundsException {
        if(!isEntranceFull()){
            students.replace(student,students.get(student)+1);
        }
        else
            throw new StudentsOutOfBoundsException();

    }

    /**
     *
     * @param c The colour of the table
     * @return  The number of students in the table of the given colour
     */
    public int getTable(Colour c) {
        return tables.get(c);
    }

    /**
     *
     * @param table The position of the table in the collection
     */
    public void addToTable (Colour table) throws StudentsOutOfBoundsException{
        if(!isTableFull(table)){
            tables.replace(table,tables.get(table)+1);
        }
        else
            throw new StudentsOutOfBoundsException();
    }

    public void addTowers(int t) throws TowerOutOfBoundException{
        if(towers+t<=towersLimit){
            towers+=t;
        }else{
            throw new TowerOutOfBoundException();
        }
    }

    public void useTowers(int t) throws TowerOutOfBoundException{
        if(towers>=t){
            towers-=t;
        }else{
            throw new TowerOutOfBoundException();
        }
    }

    public boolean isTableFull(Colour table){
        return (tables.get(table)>=10);
    }

    public boolean isEntranceFull(){
       int students= (Arrays.stream(Colour.values()))
                .map(C -> super.students.get(C))
                .reduce(0, (e1,e2)-> e1+e2);

       return students>=studLimit;
    }

    public void setFaction(Faction faction){
        this.faction = faction;
    }

    public Faction getFaction() {
        return faction;
    }

    public int getTowers() {
        return towers;
    }

    public void removeFromTable(Colour table) throws StudentsOutOfBoundsException{
        if(tables.get(table)>0){
            tables.replace(table,tables.get(table)-1);
        }
        else{
            throw new StudentsOutOfBoundsException();
        }

    }

    //checks if the students in a specified table of the board have reached a "coin" spot
    public boolean checkCoin(Colour table)
    {
        return (tables.get(table)>0 && (tables.get(table)%3==0));
    }
}
