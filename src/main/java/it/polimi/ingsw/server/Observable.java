package it.polimi.ingsw.server;

import java.util.ArrayList;
import java.util.List;

/**Observable pattern class of T contest**/
public class Observable<T> {

    /**List of observers**/
    private final List<Observer<T>> observers = new ArrayList<>();


    /**Add a new observer
     *
     * @param observer the observer
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }


    /**Remove an observer
     *
     * @param observer the observer to remove
     */
    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }


    /**Notify the contest to all observers
     *
     * @param message the contest
     */
    public void notify(T message){
        synchronized (observers) {
            for (Observer<T> observer : observers) {
                observer.update(message);
            }
        }
    }

}
