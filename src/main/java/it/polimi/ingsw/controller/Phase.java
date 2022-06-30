package it.polimi.ingsw.controller;

/**
 * The phases of an action turn
 */
public enum Phase {
    /**
     * Action phase in which players can move students from the entrance to the tables or to isles
     */
    STUDENTS,
    /**
     * Action phase in which players can move mother nature
     */
    MOTHERNATURE,
    /**
     * Action phase in which players can choose a cloud to retrieve its students
     */
    CLOUD
}
