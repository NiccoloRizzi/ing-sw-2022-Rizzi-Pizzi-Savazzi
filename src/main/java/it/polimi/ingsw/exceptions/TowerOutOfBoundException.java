package it.polimi.ingsw.exceptions;

public class TowerOutOfBoundException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Added a tower over the maximum game limit");
        printStackTrace(System.err);
    }
}
