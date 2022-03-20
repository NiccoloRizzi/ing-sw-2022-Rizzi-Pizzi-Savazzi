package it.polimi.ingsw.model;

public class Student{
    private final Colour type;

    /**
     * Default constructor
     */
    public Student(Colour type) {
        this.type = type;
    }

    public Colour getType(){
        return type;
    }
}