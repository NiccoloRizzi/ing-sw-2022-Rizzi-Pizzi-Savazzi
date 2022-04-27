package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GameModel;

public interface CheckTowerStrategy {
    void checkTower(GameModel gm, int island);
}
