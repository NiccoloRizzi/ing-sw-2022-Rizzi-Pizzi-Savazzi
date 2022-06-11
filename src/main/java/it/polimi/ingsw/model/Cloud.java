package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientCloud;

import java.util.HashMap;

public class Cloud extends Tile {

    /**
     * @param ID is the position of the cloud in the array.
     */
    public Cloud(int ID) {
        super(ID);
    }

    /**
     * @return the HashMap of students in the Cloud and empty it by setting to 0 all the number of students.
     * This function also notify the changes to the observer of the model [See notifyChange()].
     */
    public HashMap<Colour,Integer> empty() {
        HashMap<Colour, Integer> temp = new HashMap<>(students);
        for(Colour c:Colour.values())
            students.put(c,0);
        notifyChange();
        return temp;
    }

    /**
     * Notify the model observer (PLayerConnection) all the changes constructing a ClientCloud message.
     */
    @Override
    public void notifyChange()
    {
        notify(new ClientCloud(ID,new HashMap<>(students)));

    }
}