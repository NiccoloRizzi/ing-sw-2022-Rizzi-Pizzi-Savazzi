package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.GameModel;

public interface CheckProfessorStrategy {
    void checkProfessor(GameModel gm, Colour type, int currentPlayer);
}
