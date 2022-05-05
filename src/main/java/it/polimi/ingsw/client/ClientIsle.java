package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;

import java.util.HashMap;

public class ClientIsle implements ClientModel{

    final int id;
    final private Faction controlling;
    final private HashMap<Colour, Integer> students;
    final private boolean motherNature;

    public ClientIsle(ClientIsle isle) {
        this.id = isle.id;
        this.controlling = isle.controlling;
        this.students = (HashMap<Colour, Integer>) isle.students.clone();
        this.motherNature = isle.motherNature;
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

    public boolean isMotherNature() {
        return motherNature;
    }

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}
