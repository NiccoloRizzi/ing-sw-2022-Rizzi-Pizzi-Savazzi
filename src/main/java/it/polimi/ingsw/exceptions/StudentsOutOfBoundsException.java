package it.polimi.ingsw.exceptions;

/**
 * Exception given when adding students to a full tile or removing one from an empty one
 */
public class StudentsOutOfBoundsException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tried adding a student in a full collection/ removing a student from an empty one");
        printStackTrace(System.err);
    }
}
