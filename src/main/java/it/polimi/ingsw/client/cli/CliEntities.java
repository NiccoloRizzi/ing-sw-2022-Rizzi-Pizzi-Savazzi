package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ModelView;
import it.polimi.ingsw.clientModels.ClientBoard;
import it.polimi.ingsw.clientModels.ClientCharacter;
import it.polimi.ingsw.clientModels.ClientCloud;
import it.polimi.ingsw.clientModels.ClientIsle;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;

import java.util.Arrays;
import java.util.HashMap;

/**
 * A class that groups all the specifications of a CliEntity class
 */
public class CliEntities {
    /**
     * The type Cli board.
     */
    public static class CliBoard extends CliEntity {
        /**
         * Instantiates a new Cli board.
         *
         * @param modelView the model view
         * @param ID        the id of the player owner of the board
         */
        public CliBoard(ModelView modelView, int ID, boolean isExpert){
            // Init
            mtx = new Cell[22][7];
            for (Cell[] chars : mtx) {
                Arrays.fill(chars, new Cell(CellType.NULL, CliColors.BLACK));
            }
            ClientBoard board = modelView.getBoards()[ID];
            addStringToMtx(0, 1, modelView.getPlayers()[board.getPlayerID()].getNickname());
            if(isExpert){
                addStringToMtx(1, 1, "Coins: " + modelView.getPlayers()[board.getPlayerID()].getCoins());
            }
            // Set entrance
            int row = 3, col = 1;
            for(Colour c : board.getEntrance().keySet()){
                for(int i = 0; i < board.getEntrance().get(c); i++){
                    mtx[row][col] = getStudCellType(c);
                    col++;
                    if(col == 6){
                        col = 1;
                        row++;
                    }
                }
            }

            // Set tables
            for(Colour c : board.getTables().keySet()){
                col = 1 + c.ordinal();
                for(int i = 0; i < board.getTables().get(c); i++){
                    row = 15 - i;
                    mtx[row][col] = getStudCellType(c);
                }
            }
            // Set professor
            row = 17;
            for(Colour c : modelView.getGameModel().getProfessors().keySet()){
                if(modelView.getGameModel().getProfessors().get(c) == ID){
                    col = 1 + c.ordinal();
                    mtx[row][col] = getProfCellType(c);
                }
            }
            // Set towers
            row = 19;
            col = 1;
            for(int i = 0; i < board.getTowers(); i++){
                switch (board.getFaction()) {
                    case Black -> mtx[row][col] = new Cell(CellType.TOWER, CliColors.BLACK);
                    case White -> mtx[row][col] = new Cell(CellType.TOWER, CliColors.WHITE);
                    case Grey -> mtx[row][col] = new Cell(CellType.TOWER, CliColors.GREY);
                }
                col++;
                if(col == 5){
                    col = 1;
                    row++;
                }
            }
            // Set border
            // Horizontal
            for(int i = 0; i < mtx[0].length; i++){
                mtx[2][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[mtx.length - 1][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[5][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[16][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[18][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
            }
            // Vertical
            for(int i = 2; i < mtx.length; i++){
                mtx[i][0] = new Cell(CellType.BORDER_V, CliColors.GREY);
                mtx[i][mtx[0].length - 1] = new Cell(CellType.BORDER_V, CliColors.GREY);
            }
            // Corners
            mtx[2][0] = new Cell(CellType.BORDER_NO, CliColors.GREY);
            mtx[2][mtx[0].length - 1] = new Cell(CellType.BORDER_NE, CliColors.GREY);
            mtx[mtx.length - 1][0] = new Cell(CellType.BORDER_SO, CliColors.GREY);
            mtx[mtx.length - 1][mtx[0].length - 1] = new Cell(CellType.BORDER_SE, CliColors.GREY);
            // 3 Ways
            mtx[5][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
            mtx[5][mtx[0].length - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
            mtx[16][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
            mtx[16][mtx[0].length - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
            mtx[18][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
            mtx[18][mtx[0].length - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
        }
    }

    /**
     * The type Cli isle.
     */
    public static class CliIsle extends CliEntity {
        /**
         * Instantiates a new Cli isle.
         *
         * @param modelView the model view
         * @param ID        the id of the isle
         */
        public CliIsle(ModelView modelView, int ID, boolean isProhibitedPresent){
            ClientIsle isle = modelView.getGameModel().getIsles().get(ID);
            Faction faction = isle.getControlling();
            HashMap<Colour, Integer> students = isle.getStudents();
            int prohibited = isle.getProhibited();
            int size = isle.getSize();
            final int ROWS = 13;
            final int COLS = 8;
            mtx = new Cell[ROWS][COLS];
            for(Cell[] cols : mtx){
                Arrays.fill(cols, new Cell(CellType.NULL, CliColors.BLACK));
            }
            // Borders
            for(int i = 0; i < ROWS; i++){
                mtx[i][0] = new Cell(CellType.BORDER_V, CliColors.GREY);
                mtx[i][COLS - 1] = new Cell(CellType.BORDER_V, CliColors.GREY);
            }
            for(int i = 0; i < COLS; i++){
                mtx[0][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[ROWS - 1][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[3][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[9][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
            }
            mtx[3][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
            mtx[9][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
            mtx[3][COLS - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
            mtx[9][COLS - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
            mtx[0][0] = new Cell(CellType.BORDER_NO, CliColors.GREY);
            mtx[0][COLS - 1] = new Cell(CellType.BORDER_NE, CliColors.GREY);
            mtx[ROWS - 1][0] = new Cell(CellType.BORDER_SO, CliColors.GREY);
            mtx[ROWS - 1][COLS - 1] = new Cell(CellType.BORDER_SE, CliColors.GREY);
            // Isle id
            addStringToMtx(1, 1, "Isola: " + (ID + 1));
            // Towers
            switch (faction) {
                case Grey -> {
                    mtx[2][1] = new Cell(CellType.TOWER, CliColors.GREY);
                    addStringToMtx(2, 2, "" + size);
                }
                case White -> {
                    mtx[2][1] = new Cell(CellType.TOWER, CliColors.WHITE);
                    addStringToMtx(2, 2, "" + size);
                }
                case Black -> {
                    mtx[2][1] = new Cell(CellType.TOWER, CliColors.BLACK);
                    addStringToMtx(2, 2, "" + size);
                }
                default -> mtx[2][1] = new Cell(CellType.NULL, CliColors.BLACK);
            }
            // Students
            for(Colour c : Colour.values()){
                mtx[c.ordinal() + 4][1] = getStudCellType(c);
                addStringToMtx(c.ordinal() + 4, 2, "" + ((students.get(c) != null)?students.get(c):"0"));
            }
            // Prohibited
            if(isProhibitedPresent){
                mtx[10][1] = new Cell(CellType.PRHOIBITED, CliColors.RED);
                addStringToMtx(10, 2, "" + prohibited);
            }
            // Mn
            int mnPos = modelView.getGameModel().getMotherNature();
            if(mnPos == ID){
                mtx[11][1] = new Cell(CellType.MN, CliColors.BROWN);
                addStringToMtx(11, 2, "Mother Nature");
            }
        }
    }

    /**
     * The type Cli assistant.
     */
    public static class CliAssistant extends CliEntity {
        /**
         * Instantiates a new Cli assistant.
         *
         * @param modelView the model view
         * @param ID        the id of the assistant
         * @param isDeck    the flag used to choose what is assistant to generate between the ones form the deck or the array of used assistants
         * @param playerID  the player id
         */
        public CliAssistant(ModelView modelView, int ID, boolean isDeck, int playerID){
            Integer assistant;
            boolean isBoosted = false;
            if(isDeck){
                assistant = modelView.getPlayers()[playerID].getDeck()[ID];
            }else{
                assistant = modelView.getPlayers()[playerID].getUsedAssistants()[ID];
                int lastPos = modelView.getPlayers()[playerID].getUsedAssistants().length - 1;
                isBoosted = modelView.getPlayers()[playerID].isBoost() && ID == lastPos;
            }
            final int ROWS = 4;
            final int COLS = 5;
            mtx = new Cell[ROWS][COLS];
            for(Cell[] cols : mtx){
                Arrays.fill(cols, new Cell(CellType.NULL, CliColors.BLACK));
            }
            // Borders
            CliColors borderColor = (isBoosted)?CliColors.BLUE:CliColors.GREY;
            for(int i = 0; i < ROWS; i++){
                mtx[i][0] = new Cell(CellType.BORDER_V, borderColor);
                mtx[i][COLS - 1] = new Cell(CellType.BORDER_V, borderColor);
            }
            for(int i = 0; i < COLS; i++){
                mtx[0][i] = new Cell(CellType.BORDER_O, borderColor);
                mtx[ROWS - 1][i] = new Cell(CellType.BORDER_O, borderColor);
            }
            mtx[0][0] = new Cell(CellType.BORDER_NO, borderColor);
            mtx[0][COLS - 1] = new Cell(CellType.BORDER_NE, borderColor);
            mtx[ROWS - 1][0] = new Cell(CellType.BORDER_SO, borderColor);
            mtx[ROWS - 1][COLS - 1] = new Cell(CellType.BORDER_SE, borderColor);
            // Assistant info
            addStringToMtx(1, 1, "Value: " + (assistant + 1));
            addStringToMtx(2, 1, "Mn: " + ((assistant+2)/2));
        }
    }

    /**
     * The type Cli character.
     */
    public static class CliCharacter extends CliEntity {
        /**
         * Instantiates a new Cli character.
         *
         * @param modelView the model view
         * @param ID        the id of the used characters
         */
        public CliCharacter(ModelView modelView, int ID, boolean isUsed){
            ClientCharacter character = modelView.getCharacters()[ID];
            int id = character.getID();
            int price = character.getPrice();
            HashMap<Colour, Integer> students = character.getStudents();
            final int ROWS = 9;
            final int COLS = 9;
            mtx = new Cell[ROWS][COLS];
            for(Cell[] cols : mtx){
                Arrays.fill(cols, new Cell(CellType.NULL, CliColors.BLACK));
            }
            // Borders
            CliColors borderColor = (isUsed)?CliColors.YELLOW:CliColors.GREY;
            for(int i = 0; i < ROWS; i++){
                mtx[i][0] = new Cell(CellType.BORDER_V, borderColor);
                mtx[i][COLS - 1] = new Cell(CellType.BORDER_V, borderColor);
            }
            for(int i = 0; i < COLS; i++){
                mtx[0][i] = new Cell(CellType.BORDER_O, borderColor);
                mtx[ROWS - 1][i] = new Cell(CellType.BORDER_O, borderColor);
            }
            mtx[0][0] = new Cell(CellType.BORDER_NO, borderColor);
            mtx[0][COLS - 1] = new Cell(CellType.BORDER_NE, borderColor);
            mtx[ROWS - 1][0] = new Cell(CellType.BORDER_SO, borderColor);
            mtx[ROWS - 1][COLS - 1] = new Cell(CellType.BORDER_SE, borderColor);
            // Character id
            addStringToMtx(1, 1, "" + character.getCard());
            addStringToMtx(2, 1, "Price " + price);
            // Students
            if(students != null){
                for(Colour c : Colour.values()){
                    mtx[c.ordinal() + 3][1] = getStudCellType(c);
                    addStringToMtx(c.ordinal() + 3, 2, "" + students.get(c));
                }
            }
        }
    }

    /**
     * The type Cli cloud.
     */
    public static class CliCloud extends CliEntity {
        /**
         * Instantiates a new Cli cloud.
         *
         * @param modelView the model view
         * @param ID        the id of the cloud
         */
        public CliCloud(ModelView modelView, int ID){
            ClientCloud cloud = modelView.getClouds()[ID];
            int id = cloud.getId();
            HashMap<Colour, Integer> students = cloud.getStudents();
            final int ROWS = 8;
            final int COLS = 5;
            mtx = new Cell[ROWS][COLS];
            for(Cell[] cols : mtx){
                Arrays.fill(cols, new Cell(CellType.NULL, CliColors.BLACK));
            }
            // Borders
            for(int i = 0; i < ROWS; i++){
                mtx[i][0] = new Cell(CellType.BORDER_V, CliColors.GREY);
                mtx[i][COLS - 1] = new Cell(CellType.BORDER_V, CliColors.GREY);
            }
            for(int i = 0; i < COLS; i++){
                mtx[0][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
                mtx[ROWS - 1][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
            }
            mtx[0][0] = new Cell(CellType.BORDER_NO, CliColors.GREY);
            mtx[0][COLS - 1] = new Cell(CellType.BORDER_NE, CliColors.GREY);
            mtx[ROWS - 1][0] = new Cell(CellType.BORDER_SO, CliColors.GREY);
            mtx[ROWS - 1][COLS - 1] = new Cell(CellType.BORDER_SE, CliColors.GREY);
            // Cloud id
            addStringToMtx(1, 1, "Cloud " + (id + 1));
            // Students
            if(students != null){
                for(Colour c : Colour.values()){
                    mtx[c.ordinal() + 2][1] = getStudCellType(c);
                    addStringToMtx(c.ordinal() + 2, 2, "" + students.get(c));
                }
            }
        }
    }

    /**
     * The type Cli string: it's used to represent a string as a matrix
     */
    public static class CliString extends CliEntity{
        /**
         * Instantiates a new Cli string.
         *
         * @param string the string
         */
        public CliString(String string){
            mtx = new Cell[1][string.length() / 3 + 1];
            Arrays.fill(mtx[0], new Cell(CellType.CHAR, CliColors.WHITE, "   "));
            addStringToMtx(0,0, string);
        }
    }
}
