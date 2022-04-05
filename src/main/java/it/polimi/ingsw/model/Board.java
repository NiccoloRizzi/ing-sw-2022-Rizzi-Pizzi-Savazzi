package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;

public class Board extends Tile{
    private Faction faction;
    private int towers;
    private final int towersLimit;
    private final int []tables;
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
        tables = new int[5];
    }

    /**
     * Removes a student from the entrance.
     * @param i Position of the student in the collection
     */
    public void removeStudent(int i) /* throws Exception*/ {
        super.students.remove(i);
    }

    /**
     *
     * @param student The student that will be added to the player's entrance
     * @throws StudentsOutOfBoundsException When adding a student to a full entrance
     */
    public void addToEntrance(Student student) throws StudentsOutOfBoundsException {
        if(super.students.size()<this.studLimit){
            super.students.add(student);
        }
        throw(new StudentsOutOfBoundsException());
    }

    /**
     *
     * @param c The colour of the table
     * @return  The number of students in the table of the given colour
     */
    public int getTable(Colour c) {
        return tables[c.ordinal()];
    }

    /**
     *
     * @param table The position of the table in the collection
     */
    public void addToTable (int table) throws StudentsOutOfBoundsException{
        if(!isTableFull(table)){
            tables[table]++;
        }
        throw new StudentsOutOfBoundsException();
    }

    //used to add back a tower from an Isle who gets taken by another player
    public boolean addTower() /*throws Exception*/{
        if(towers<towersLimit){
            towers++;
            return true;
        }
        return false;
    }

    //when a player builds a tower on an isle, it gets removed from his board
    public boolean useTower() /*throws Exception*/{
        if(towers>0){
            towers--;
            return true;
        }
        return false;
    }

    public boolean isTableFull(int table){
        return (tables[table]==10);
    }

    public boolean isEntranceFull(){
        return super.students.size()==studLimit;
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

    public boolean removeFromTable(int table){
        if(tables[table]>0){
            tables[table]--;
            return true;
        }
        return false;
    }

    //checks if the students in a specified table of the board have reached a "coin" spot
    public boolean checkCoin(int table)
    {
        return (tables[table]>0 && (tables[table])%3==0);
    }
}
