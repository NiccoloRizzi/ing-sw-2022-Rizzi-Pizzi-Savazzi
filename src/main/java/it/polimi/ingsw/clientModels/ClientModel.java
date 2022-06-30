package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;

/**
 * Interface for Client-side version of Game models that can be sent as updates
 */
public interface ClientModel {
    /**
     * Accept method for visitor pattern
     * @param visitor Client-view, acting as a Visitor for server updates
     */
    void accept(View visitor);
    /**
     * Method for serializing messages in Json format
     * @return Serialized message in Json Format
     */
    String serialize();
}
