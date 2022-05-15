package it.polimi.ingsw.clientModels;

import it.polimi.ingsw.client.View;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.server.ModelSerializer;

import java.util.HashMap;

public class ClientBoard implements ClientModel{

    private int playerID;
    private final Faction faction;
    private final int towers;
    private final HashMap<Colour, Integer> tables;
    private final HashMap<Colour, Integer> entrance;

    public ClientBoard(int playerID,Faction faction,int towers,HashMap<Colour,Integer> tables,HashMap<Colour,Integer> entrance)
    {
        this.playerID = playerID;
        this.faction = faction;
        this.towers = towers;
        this.tables = tables;
        this.entrance = entrance;
    }

    public int getPlayerID() {
        return playerID;
    }

    public Faction getFaction() {
        return faction;
    }

    public int getTowers() {
        return towers;
    }

    public HashMap<Colour, Integer> getTables() {
        return tables;
    }

    public HashMap<Colour, Integer> getEntrance() {
        return entrance;
    }

    public ClientBoard(ClientBoard board){
        this.playerID = board.playerID;
        this.entrance = new HashMap<>(board.entrance);
        this.faction = board.faction;
        this.towers = board.towers;
        this.tables = new HashMap<>(board.tables);
    }

    @Override
    public String serialize(){
        return ModelSerializer.serialize(this);
    }

    @Override
    public void accept(View view) {
        view.visit(this);
    }
}
