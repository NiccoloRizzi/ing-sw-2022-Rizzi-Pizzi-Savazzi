package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;

public class WinMessage implements ClientModel {
    int id;

    public WinMessage(int id){
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void accept(View visitor)
    {
        visitor.visit(this);
    }
}
