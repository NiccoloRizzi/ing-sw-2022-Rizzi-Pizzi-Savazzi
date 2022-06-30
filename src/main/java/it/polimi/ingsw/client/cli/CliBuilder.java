package it.polimi.ingsw.client.cli;

import java.util.Optional;

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
    void setBoards(boolean isExpert);

    /**
     * Create a CliEntity that represents all the isles
     */
    void setIsles(boolean isProhibitedPresent);

    /**
     * Create a CliEntity that represents all the active characters
     * @param usedCharacter ID of the usedcharacter if it exists
     */
    void setCharacter(Optional<Integer> usedCharacter);

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
     * Create a CliEntity that show current turn
     */
    void setTurn();

    /**
     * Create a CliEntity that show current and errors if present
     */
    void setErrors();

    void setWin();

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
