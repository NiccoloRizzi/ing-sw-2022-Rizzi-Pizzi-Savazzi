package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientGameModel implements ClientModel{

    private HashMap<Colour, Integer> professors;
    private int motherNature;
    private ArrayList<ClientIsle> isles;
    private int prohibited;

    public ClientGameModel(HashMap<Colour, Integer> professors, int motherNature, ArrayList<ClientIsle> isles, int prohibited) {
        this.professors = professors;
        this.motherNature = motherNature;
        this.isles = isles;
        this.prohibited = prohibited;
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

    public int getProhibited() {
        return prohibited;
    }
    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }
}
