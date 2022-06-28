package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;

/**
 * Interface of the strategy used when checking what tower should be placed on an Isle
 */
public interface CheckTowerStrategy {
    /**
     * Determines which team or player should place a tower on the isle
     * @param gm The gamemodel
     * @param island The island being checked
     */
    void checkTower(GameModel gm, int island);
}
