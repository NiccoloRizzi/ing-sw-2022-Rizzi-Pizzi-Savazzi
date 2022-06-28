package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when there aren't enough coins to perform an action or coins number are not coherent with game limits
 */
public class NotEnoughCoinsException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tried using more coins then there are.");
        printStackTrace(System.err);
    }
}
