package it.polimi.ingsw.model;

public class Assistant {

    private final int value;
    private final int mn_moves;

    public Assistant( int value, int mn_moves) {
        this.value = value;
        this.mn_moves = mn_moves;
    }

    public int getValue() {
        return value;
    }

    public int getMn_moves() {
        return mn_moves;
    }
}
