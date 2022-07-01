package it.polimi.ingsw.client.cli;

/**
 * A class that represent the complete cli to be printed
 */
public class CliModel {
    /**Represents all the boards to print**/
    CliEntity boards;
    /**Represents all the isles to print**/
    CliEntity isles;
    /**Represents all the characters to print**/
    CliEntity characters;
    /**Represents all the clouds to print**/
    CliEntity clouds;
    /**Represents all the assistants to print**/
    CliEntity assistants;
    /**Represents all the assistants of other players to print**/
    CliEntity otherPlayerAss;
    /**Represents the turn message to print**/
    CliEntity turn;
    /**Represents the error message to print**/
    CliEntity errors;
    /**Represents the win message to print**/
    CliEntity win;
    /**Represents the full cli to print**/
    CliEntity fullCli;

    /**
     * Instantiates a new Cli model.
     */
    public CliModel(){
        boards = new CliEntity();
        isles = new CliEntity();
        characters = new CliEntity();
        clouds = new CliEntity();
        assistants = new CliEntity();
        turn = new CliEntity();
        errors = new CliEntity();
        fullCli = new CliEntity();
        otherPlayerAss = new CliEntity();
        win = new CliEntity();
    }

    /**
     * Sets boards.
     *
     * @param boards the boards
     */
    public void setBoards(CliEntity boards) {
        this.boards = boards;
    }

    /**
     * Sets isles.
     *
     * @param isles the isles
     */
    public void setIsles(CliEntity isles) {
        this.isles = isles;
    }

    /**
     * Gets other player assistants.
     *
     * @return the other player ass
     */
    public CliEntity getOtherPlayerAss() {
        return otherPlayerAss;
    }

    /**
     * Sets characters.
     *
     * @param characters the characters
     */
    public void setCharacters(CliEntity characters) {
        this.characters = characters;
    }

    /**
     * Sets clouds.
     *
     * @param clouds the clouds
     */
    public void setClouds(CliEntity clouds) {
        this.clouds = clouds;
    }

    /**
     * Sets assistants.
     *
     * @param assistants the assistants
     */
    public void setAssistants(CliEntity assistants) {
        this.assistants = assistants;
    }

    /**
     * Sets turn entity.
     *
     * @param turn the turn and errors entity
     */

    public void setTurn(CliEntity turn) {
        this.turn = turn;
    }

    /**
     * Sets errors entity.
     *
     * @param errors the turn and errors entity
     */

    public void setErrors(CliEntity errors) {
        this.errors = errors;
    }

    public void setWin(CliEntity win) {
        this.win = win;
    }

    /**
     * Sets other player assistants.
     *
     * @param otherPlayerAss the other player assistants
     */
    public void setOtherPlayerAss(CliEntity otherPlayerAss) {
        this.otherPlayerAss = otherPlayerAss;
    }

    /**
     * Sets full cli.
     *
     * @param fullCli the full cli
     */
    public void setFullCli(CliEntity fullCli) {
        this.fullCli = fullCli;
    }

    /**
     * Gets boards.
     *
     * @return the boards
     */
    public CliEntity getBoards() {
        return boards;
    }

    /**
     * Gets isles.
     *
     * @return the isles
     */
    public CliEntity getIsles() {
        return isles;
    }

    /**
     * Gets characters.
     *
     * @return the characters
     */
    public CliEntity getCharacters() {
        return characters;
    }

    /**
     * Gets clouds.
     *
     * @return the clouds
     */
    public CliEntity getClouds() {
        return clouds;
    }

    /**
     * Gets assistants.
     *
     * @return the assistants
     */
    public CliEntity getAssistants() {
        return assistants;
    }

    /**
     * Gets turn entity
     *
     * @return the turn and errors entity
     */
    public CliEntity getTurn() {
        return turn;
    }

    /**
     * Gets errors entity
     *
     * @return the turn and errors entity
     */
    public CliEntity getErrors() {
        return errors;
    }


    /**
     * Get win message entity
     * @return the winn message entity
     */
    public CliEntity getWin() {
        return win;
    }

    /**
     * Print the cli
     */
    public void print(){
        fullCli.print();
    }
}
