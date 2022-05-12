package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientIsle;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashMap;

public class CharacterStudents extends Character {
    private final HashMap<Colour, Integer> students;

    public CharacterStudents (int id,CharactersEnum character)
    {
        super(id,character);
        students = new HashMap<>();
        for(Colour c: Colour.values()){
            students.put(c,0);
        }
    }

    public void addStudent(Colour student)
    {
        students.replace(student, students.get(student)+1);
    }

    public int getStudents(Colour c) { return students.get(c);}

    public void addStudents(HashMap<Colour, Integer> studentsToAdd) {
        for(Colour c : Colour.values()){
            students.replace(c, students.get(c) + studentsToAdd.get(c));
        }
        notifyChange();
    }
    public  void removeStudent(Colour c) throws StudentsOutOfBoundsException{
        if(students.get(c)>0) {
            students.replace(c, students.get(c) - 1);
            notifyChange();
        }
        else throw new StudentsOutOfBoundsException();
    }

    @Override
    public void notifyChange()
    {
        HashMap<Colour,Integer> temp = new HashMap<>(students);
        notify(new ClientCharacter(ID,card,price,temp));
    }
}
