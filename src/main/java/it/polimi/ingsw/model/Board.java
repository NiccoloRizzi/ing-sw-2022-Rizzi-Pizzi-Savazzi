package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Board extends Tile{
    private Faction faction;
    private int towers;
    private int tables[];
    private boolean professors[];

    //the number of starting towers depends on the number of players
    public Board(Faction faction, int towers) {
        this.faction = faction;
        this.towers = towers;
        tables = new int[4];
        professors = new boolean[4];
    }

    //removes a student from the entrance
    public void removeStudent(int i) {
        super.students.remove(i);
    }


    public int getTable(Colour c) {
        return tables[c.ordinal()];
    }

    //adds a student TO A SPECIFIED TABLE
    public boolean addToTable(int table){
        if(!isFull(table)){
            tables[table]++;
            return true;
        }
        return false;
    }

    public boolean isFull(int table){
        return (tables[table]==10);
    }

    public void setFaction(Faction faction){
        this.faction = faction;
    }

    public void setProfessor(int table, boolean isThere){
        professors[table]=isThere;
    }

    //returns the list of professors currently on this board
    public boolean[] getProfessors() {
        return professors.clone();
    }

    public Faction getFaction() {
        return faction;
    }

    public int getTowers() {
        return towers;
    }

    //checks if the students in a specified table of the board have reached a "coin" spot
    public boolean checkCoin(int table){
        return (tables[table]%3)==0;
    }
}
