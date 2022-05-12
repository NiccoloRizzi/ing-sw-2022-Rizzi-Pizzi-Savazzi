package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientGameModel implements ClientModel{

    HashMap<Colour, Integer> professors;
    int motherNature;
    private ArrayList<ClientIsle> isles;

    public ClientGameModel(HashMap<Colour, Integer> professors, int motherNature, ArrayList<ClientIsle> isles) {
        this.professors = professors;
        this.motherNature = motherNature;
        this.isles = isles;
    }

    public ArrayList<ClientIsle> getIsles() {
        return isles;
    }

    public HashMap<Colour, Integer> getProfessors() {
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
