package it.polimi.ingsw.client;

public class ClientPlayer implements ClientModel{

    public ClientPlayer(ClientPlayer player){

    }

    @Override
    public void accept(View visitor) {
        visitor.visit(this);
    }
}
