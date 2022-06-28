package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when adding more players to the match than the number chosen in the game setup
 */
public class PlayerOutOfBoundException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tried adding more player over than maximum limit based on current rules.");
        printStackTrace(System.err);
    }
}