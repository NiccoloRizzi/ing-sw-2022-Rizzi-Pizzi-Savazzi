package it.polimi.ingsw.exceptions;

public class StudentsOutOfBoundsException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tried adding a student in a full collection/ removing a student from an empty one");
        printStackTrace(System.err);
    }
}
