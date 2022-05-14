package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;

public class WinMessage {
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
