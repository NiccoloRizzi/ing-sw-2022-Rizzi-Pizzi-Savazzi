package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;

import java.util.HashMap;

public class ClientIsle implements ClientModel{

    private final int id;
    private final Faction controlling;
    private final HashMap<Colour, Integer> students;

    private final int prohibited;

    public ClientIsle(int id,Faction controlling,HashMap<Colour, Integer> students,int prohibited) {
        this.id = id;
        this.controlling = controlling;
        this.students = students;
        this.prohibited=prohibited;
    }

    public int getId() {
        return id;
    }

    public Faction getControlling() {
        return controlling;
    }

    public HashMap<Colour, Integer> getStudents() {
        return students;
    }

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}