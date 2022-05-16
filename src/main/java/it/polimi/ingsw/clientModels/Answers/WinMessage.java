package it.polimi.ingsw.clientModels.Answers;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.server.ModelSerializer;

public class WinMessage implements ClientModel {
    private int id;
    private final boolean draw;
    public WinMessage(int id){
        this.id = id;
        this.draw = false;
    }
    public WinMessage(boolean draw){
        this.draw = draw;
    }

    public int getId() {
        return id;
    }

    public void accept(View visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }

    public boolean isDraw() {
        return draw;
    }
}
