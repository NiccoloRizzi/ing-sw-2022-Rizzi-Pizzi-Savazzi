package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when adding towers to a full tile or removing one from an empty tile
 */
public class TowerOutOfBoundException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Added a tower over the maximum game limit");
        printStackTrace(System.err);
    }
}
