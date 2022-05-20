package it.polimi.ingsw.client.cli;

/**
 * The interface Cli builder.
 */
public interface CliBuilder {
    /**
     * Regenerate the cli model
     */
    void reset();

    /**
     * Create a CliEntity that represents all the boards
     */
    void setBoards();

    /**
     * Create a CliEntity that represents all the isles
     */
    void setIsles();

    /**
     * Create a CliEntity that represents all the active characters
     */
    void setCharacter();

    /**
     * Create a CliEntity that represents all the clouds
     */
    void setClouds();

    /**
     * Create a CliEntity that represents all the player's assistants in the deck
     */
    void setAssistants();

    /**
     * Create a CliEntity that represents all the used assistants
     */
    void setUsedAssistants();

    /**
     * Create a CliEntity that represents all the used assistants of other players
     */
    void setOtherPlayersAss();

    /**
     * Create a CliEntity that show current turn and errors if present
     */
    void setTurnAndError();

    /**
     * Comprensive function that calls the sets functions that the concrete builder want to use
     */
    void setAllCli();

    /**
     * Link all the set Cli entities
     */
    void composeCLi();

    /**
     * Gets the cli model
     *
     * @return the cli
     */
    CliModel getCli();
}
