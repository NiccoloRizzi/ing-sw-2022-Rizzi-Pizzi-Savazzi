package it.polimi.ingsw.model;
/**
 * Class that represents assistants cards
 */
public class Assistant {

    /**
     * The value of the assistant
     */
    private final int value;
    /**
     * The maximum number of steps it allows Mother Nature to be moved for
     */
    private final int mn_moves;
    /**
     * Used when an assistant is boosted by a character
     */
    private int boost;

    /**
     * Constructs an assistant and sets boost to 0.
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
