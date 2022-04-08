package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashMap;

public class CharacterStudents extends Character {
    private final HashMap<Colour, Integer> students;

    public CharacterStudents (int id, int price)
    {
        super(id,price);
        students = new HashMap<>();
    }

    public void addStudent(Colour student)
    {
        students.replace(student, students.get(student)+1);
    }

    public int getStudents(Colour c) { return students.get(c);}

    public void addStudents(HashMap<Colour, Integer> studentsToAdd) {
        students.forEach((c,v)->v+=studentsToAdd.get(c));
    }
    public  void removeStudent(Colour c) throws StudentsOutOfBoundsException{
        if(students.get(c)>0)
            students.replace(c,students.get(c)-1);
        else throw new StudentsOutOfBoundsException();
    }
}
