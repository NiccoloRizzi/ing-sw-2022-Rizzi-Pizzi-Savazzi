package it.polimi.ingsw.exceptions;

public class TileOutOfBoundsException extends Exception{
    @Override
    public void printStackTrace() {
        System.out.println("Tile out of index");
    }
}
