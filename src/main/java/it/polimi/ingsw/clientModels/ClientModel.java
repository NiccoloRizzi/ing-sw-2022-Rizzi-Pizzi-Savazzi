package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;

public interface ClientModel {
    void accept(View visitor);
}
