package it.polimi.ingsw.model;

public class Assistant {

    private final int value;
    private final int mn_moves;
    private int boost;

    public Assistant( int value, int mn_moves) {
        this.value = value;
        this.mn_moves = mn_moves;
        boost = 0;
    }

    public int getValue() {
        return value;
    }

    public int getMn_moves() {
        return mn_moves;
    }

    public int getBoost(){ return boost;}

    public void Boost(){ boost = 2;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assistant assistant = (Assistant) o;

        return value == assistant.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
