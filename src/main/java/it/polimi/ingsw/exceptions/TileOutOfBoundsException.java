package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when trying to perform an action on a non-existing tile
 */
public class TileOutOfBoundsException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tile out of index");
        printStackTrace(System.err);
    }
}
