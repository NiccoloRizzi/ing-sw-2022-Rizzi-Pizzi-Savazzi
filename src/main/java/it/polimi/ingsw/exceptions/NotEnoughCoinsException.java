package it.polimi.ingsw.exceptions;

public class NotEnoughCoinsException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tried using more coins then there are.");
    }
}
