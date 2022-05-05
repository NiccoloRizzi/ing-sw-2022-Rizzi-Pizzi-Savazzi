package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;

import java.util.HashMap;

public class ClientBoard implements ClientModel{

    private final Faction faction;
    private final int towers;
    private final HashMap<Colour, Integer> tables;
    private final HashMap<Colour, Integer> entrance;

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
        this.entrance = (HashMap<Colour, Integer>) board.entrance.clone();
        this.faction = board.faction;
        this.towers = board.towers;
        this.tables = (HashMap<Colour, Integer>) board.tables.clone();
    }

    @Override
    public void accept(View view) {
        view.visit(this);
    }
}
