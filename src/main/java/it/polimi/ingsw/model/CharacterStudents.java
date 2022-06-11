package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientIsle;
import it.polimi.ingsw.exceptions.StudentsOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashMap;

public class CharacterStudents extends Character {
    private final HashMap<Colour, Integer> students;

    /**
     * @param id is the character position in the active characters array.
     * @param character is the character type from CharacterEnum.
     */
    public CharacterStudents (int id,CharactersEnum character)
    {
        super(id,character);
        students = new HashMap<>();
        for(Colour c: Colour.values()){
            students.put(c,0);
        }
    }

    /**
     * @param student is the colour of the student to add in the character.
     * This function also notify the changes to the observer of the model [See notifyChange()].
     */
    public void addStudent(Colour student)
    {
        students.replace(student, students.get(student)+1);
        notifyChange();
    }

    /**
     * @param c is the colour of the student.
     * @return the number of the student of the specified colour.
     */
    public int getStudents(Colour c) { return students.get(c);}

    /**
     * @param studentsToAdd is an HashMap of students and their number to add to the current students in the card.
     * This function also notify the changes to the observer of the model [See notifyChange()].
     */
    public void addStudents(HashMap<Colour, Integer> studentsToAdd) {
        for(Colour c : Colour.values()){
            students.replace(c, students.get(c) + studentsToAdd.get(c));
        }
        notifyChange();
    }

    /**
     * Remove a student of the specified colour form the card.
     * This function also notify the changes to the observer of the model [See notifyChange()].

     * @param c is the colour of the student to remove.
     * @throws StudentsOutOfBoundsException if there are no students to remove.
     */
    public  void removeStudent(Colour c) throws StudentsOutOfBoundsException{
        if(students.get(c)>0) {
            students.replace(c, students.get(c) - 1);
            notifyChange();
        }
        else throw new StudentsOutOfBoundsException();
    }

    /**
     * Notify the model observer (PLayerConnection) all the changes constructing a ClientCharacter message.
     */
    @Override
    public void notifyChange()
    {
        HashMap<Colour,Integer> temp = new HashMap<>(students);
        notify(new ClientCharacter(ID,card,price,temp));
    }
}
