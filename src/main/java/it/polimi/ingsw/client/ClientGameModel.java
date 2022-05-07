package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Colour;

import java.util.HashMap;

public class ClientGameModel implements ClientModel{

    HashMap<Colour, ClientPlayer> professors;
    int motherNature;
    private ClientIsle[] isles;

    public ClientIsle[] getIsles() {
        return isles;
    }

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}
