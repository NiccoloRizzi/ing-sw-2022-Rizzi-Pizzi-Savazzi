package it.polimi.ingsw.exceptions;

public class PlayerOutOfBoundException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tried adding more player over tha maximum limit based on current rules.");
        printStackTrace(System.err);
    }
}