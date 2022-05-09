package it.polimi.ingsw.server;

public interface Observer<T> {

    void update(T message);

}