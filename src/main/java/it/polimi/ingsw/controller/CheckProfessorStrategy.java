package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.GameModel;

/**
 * Interface of the strategy for checking who owns a professor
 */
public interface CheckProfessorStrategy {
    /**
     * Checks who should be the owner of a professor of a given colour
     * @param gm The gameModel
     * @param type The colour to check
     * @param currentPlayer Current player who causes the check to be performed
     */
    void checkProfessor(GameModel gm, Colour type, int currentPlayer);
}
