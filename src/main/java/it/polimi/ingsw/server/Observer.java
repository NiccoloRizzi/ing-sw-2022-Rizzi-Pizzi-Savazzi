package it.polimi.ingsw.server;

/**Observer pattern interface**/
public interface Observer<T> {

    /**Update from the contest
     *
     * @param message the contest
     */
    void update(T message);

}