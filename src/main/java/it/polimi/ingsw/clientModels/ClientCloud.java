package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;

import java.util.HashMap;

public class ClientCloud implements ClientModel{

    private int id;
    private HashMap<Colour, Integer> students;

    public ClientCloud (int id, HashMap<Colour, Integer> students)
    {
        this.id = id;
        this.students = students;
    }
    public int getId() {
        return id;
    }
    public HashMap<Colour, Integer> getStudents() {
        return students;
    }

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}
