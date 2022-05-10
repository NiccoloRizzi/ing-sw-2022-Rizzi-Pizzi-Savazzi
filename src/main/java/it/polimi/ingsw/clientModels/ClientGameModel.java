package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;

import java.util.HashMap;

public class ClientGameModel implements ClientModel{

    HashMap<Colour, ClientPlayer> professors;
    int motherNature;
    private ClientIsle[] isles;

    public ClientGameModel(HashMap<Colour, ClientPlayer> professors, int motherNature, ClientIsle[] isles) {
        this.professors = professors;
        this.motherNature = motherNature;
        this.isles = isles;
    }

    public ClientIsle[] getIsles() {
        return isles;
    }

    public HashMap<Colour, ClientPlayer> getProfessors() {
        return professors;
    }

    public int getMotherNature() {
        return motherNature;
    }

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}
