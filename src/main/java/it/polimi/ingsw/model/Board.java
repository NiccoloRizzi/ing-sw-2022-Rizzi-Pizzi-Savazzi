package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board extends Tile{
    private Faction faction;
    private int towers;
    private final int towersLimit;
    private final int []tables;
    private final int studLimit;

    //the number of starting towers depends on the number of players
    public Board(Faction faction, int towers) {
        super();
        this.faction = faction;
        this.towers = towers;
        this.studLimit = (towers == 6)?9:7;
        this.towersLimit = towers;
        tables = new int[5];
    }

    //removes a student from the entrance
    public void removeStudent(int i) /* throws Exception*/ {
        super.students.remove(i);
    }

    public boolean addToEntrance(Student student) throws StudentsOutOfBoundsException{
        if(super.students.size()<this.studLimit){
            super.students.add(student);
            return true;
        }
        throw(new StudentsOutOfBoundsException());
    }

    public int getTable(Colour c) {
        return tables[c.ordinal()];
    }

    //adds a student TO A SPECIFIED TABLE
    public boolean addToTable(int table){
        if(!isTableFull(table)){
            tables[table]++;
            return true;
        }
        return false;
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
