package it.polimi.ingsw.model;

import java.util.ArrayList;

public class CharacterStudents extends Character {
    private ArrayList<Student> students;

    public CharacterStudents (int id, int price)
    {
        super(id,price);
        students = new ArrayList<Student> ();
    }

    public void addStudent(Student student)
    {
        students.add(student);
    }
    public Student getStudent(int i) { return students.get(i);}
    public void addStudents(ArrayList<Student> students)
    {
        this.students.addAll(students);
    }
    public Student removeStudent(int i)
    {
        return students.remove(i);
    }
}
