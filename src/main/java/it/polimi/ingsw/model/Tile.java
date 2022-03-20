package it.polimi.ingsw.model;

public class Tile
{
    protected ArrayList<Student> students;

    public Tile ()
    {
        students = new ArrayList<Student>();
    }

    public void addStudent(Student student)
    {
        students.add(student);
    }

    public void addStudents(ArrayList<Student> students)
    {
        this.students.addAll(students);
    }
}