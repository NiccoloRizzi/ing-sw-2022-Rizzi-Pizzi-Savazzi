package it.polimi.ingsw.model;

public class Assistant {

    private final int value;
    private final int mn_moves;
    private int boost;

    /**
     * Constructs an assistant and set boost to 0.
     * Boost represents the surplus value of moves after the usage of the CharacterEnum.PLUS_2_MN character.
     *
     * @param value is the card identification number.
     * @param mn_moves are the max possible moves for mother nature.
     */
    public Assistant( int value, int mn_moves) {
        this.value = value;
        this.mn_moves = mn_moves;
        boost = 0;
    }


    /**
     * Get the assistant identification number.
     *
     * @return the assistant identification number.
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the number of maximum mother nature moves.
     *
     * @return the number of maximum mother nature moves.
     */
    public int getMn_moves() {
        return mn_moves;
    }

    /**
     * Get the assistant boost value (surplus of mother nature moves).
     *
     * @return the assistant boost value (surplus of mother nature moves).
     */
    public int getBoost(){ return boost;}

    /**
     * Set the boost value (surplus of mother nature moves) to 2.
     */
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
