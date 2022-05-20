package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Colour;

import java.util.Arrays;

/**
 * A class that represents a cli entity: a printable matrix that represents a game model
 */
public class CliEntity {

    /**
     * This class represents a single cell of the CliEntity matrix: every cell consists of a type that describes how it is printed,
     * a color and an attribute which is used only for CHAR types that are used to insert valid strings in the matrix
     */
    class Cell{
        private final CellType type;
        private final CliColors color;
        private final String attribute;

        /**
         * Instantiates a new Cell.
         *
         * @param type  the cell type
         * @param color the color
         */
        public Cell(CellType type, CliColors color){
            this.type = type;
            this.color = color;
            attribute = null;
        }

        /**
         * Instantiates a new Cell.
         *
         * @param type      the cell type
         * @param color     the color
         * @param attribute the attribute/what will be printed
         */
        public Cell(CellType type, CliColors color, String attribute){
            this.type = type;
            this.color = color;
            this.attribute = attribute;
        }
    }

    /**
     * The enum Cli colors.
     */
    enum CliColors{
        /**
         * Red cli colors.
         */
        RED("\u001b[31m"),
        /**
         * Green cli colors.
         */
        GREEN("\u001b[32m"),
        /**
         * Pink cli colors.
         */
        PINK("\u001b[35m"),
        /**
         * Grey cli colors.
         */
        GREY("\u001b[37m"),
        /**
         * Black cli colors.
         */
        BLACK("\u001b[30m"),
        /**
         * White cli colors.
         */
        WHITE("\u001b[97m"),
        /**
         * Yellow cli colors.
         */
        YELLOW("\u001b[93m"),
        /**
         * Blue cli colors.
         */
        BLUE("\u001b[34m"),
        /**
         * Brown cli colors.
         */
        BROWN("\u001b[33m"),
        /**
         * Clear cli colors.
         */
        CLEAR("\u001b[0m");

        private final String value;

        CliColors(String value){
            this.value = value;
        }
    }

    /**
     * The enum of Cell type
     */
    enum CellType{
        /**
         * Null cell type: the default background
         */
        NULL(" " + " " + " "),
        /**
         * Border vertical cell type.
         */
        BORDER_V(" " + "\u2551" + " "),
        /**
         * Border horizontal cell type.
         */
        BORDER_O("\u2550" + "\u2550" + "\u2550"),
        /**
         * Border nord_est cell type.
         */
        BORDER_NE("\u2550" + "\u2557" + " "),
        /**
         * Border sud-est cell type.
         */
        BORDER_SE("\u2550" + "\u255d" + " "),
        /**
         * Border nord-ovest cell type.
         */
        BORDER_NO(" " + "\u2554" + "\u2550"),
        /**
         * Border sud-ovest cell type.
         */
        BORDER_SO(" " + "\u255a" + "\u2550"),
        /**
         * Three-way-right border cell type
         */
        BORDER_3D(" " + "\u2560" + "\u2550"),
        /**
         * Three-way-left border cell type
         */
        BORDER_3S("\u2550" + "\u2563" + " "),
        /**
         * Student cell type.
         */
        STUDENT(" " + "\u25cf" + " "),
        /**
         * Prof cell type.
         */
        PROF(" " + "\u25a0" + " "),
        /**
         * Tower cell type.
         */
        TOWER(" " + "\u25b2" + " "),
        /**
         * Char cell type: used to print strings in the matrix
         */
        CHAR(" " +" " +" "),
        /**
         * Prhoibited cell type.
         */
        PRHOIBITED(" " + "#" + " "),
        /**
         * Mother nature cell type.
         */
        MN(" " + "\u25e6" + " ");

        private final String value;

        CellType(String value) {
            this.value = value;
        }
    }

    /**
     * The CliEntity matrix: every cell will be printed in the terminal to show images and/or strings
     */
    Cell[][] mtx;

    /**
     * Instantiates a new Cli entity.
     *
     * @param row the row
     * @param col the col
     */
    public CliEntity(int row, int col){
        mtx = new Cell[row][col];
    }

    /**
     * Instantiates a new Cli entity.
     */
    public CliEntity(){
        mtx = new Cell[0][0];
    }

    /**
     * A utility function that returns a valid student cell type based on the colour of the student
     *
     * @param c the colur
     * @return the stud cell type
     */
    Cell getStudCellType(Colour c) {
        switch (c){
            case Dragons: return new Cell(CellType.STUDENT, CliColors.RED);
            case Gnomes: return new Cell(CellType.STUDENT, CliColors.YELLOW);
            case Fairies: return new Cell(CellType.STUDENT, CliColors.PINK);
            case Frogs: return new Cell(CellType.STUDENT, CliColors.GREEN);
            case Unicorns: return new Cell(CellType.STUDENT, CliColors.BLUE);
            default: return null;
        }
    }

    /**
     * A utility function that returns a valid prof cell type based on the colour of the prof
     *
     * @param c the colour
     * @return the prof cell type
     */
    Cell getProfCellType(Colour c) {
        switch (c){
            case Dragons: return new Cell(CellType.PROF, CliColors.RED);
            case Gnomes: return new Cell(CellType.PROF, CliColors.YELLOW);
            case Fairies: return new Cell(CellType.PROF, CliColors.PINK);
            case Frogs: return new Cell(CellType.PROF, CliColors.GREEN);
            case Unicorns: return new Cell(CellType.PROF, CliColors.BLUE);
            default: return null;
        }
    }

    /**
     * Add string to mtx: add a string from (i,j) position of the mtx
     *  @param i      the row
     * @param j      the column
     * @param string the string
     */
    void addStringToMtx(int i, int j, String string){
        for(int c = 0; c < string.length(); c += 3){
            if(c + 2 < string.length())
                mtx[i][j + c / 3] = new Cell(CellType.CHAR, CliColors.WHITE, "" + string.charAt(c) + string.charAt(c + 1) + string.charAt(c + 2));
            else if (c + 1 < string.length())
                mtx[i][j + c / 3] = new Cell(CellType.CHAR, CliColors.WHITE, "" + string.charAt(c) + string.charAt(c + 1) + " ");
            else
                mtx[i][j + c / 3] = new Cell(CellType.CHAR, CliColors.WHITE, "" + string.charAt(c) + "  ");
        }
    }

    /**
     * @return true if the CliEntity is degenerate, i.e. it is one or zero dimensional
     */
    private boolean isDegenerate(){
        return mtx.length == 0 || mtx[0].length == 0;
    }

    /**
     * Stick on the left a CliEntity
     *
     * @param cliEntity the cli entity
     * @return the new cli entity
     */
    public CliEntity rightStick(CliEntity cliEntity){
        if(this.isDegenerate() && !cliEntity.isDegenerate()) return cliEntity;
        if(cliEntity.isDegenerate() && !this.isDegenerate()) return this;
        if(cliEntity.isDegenerate()) return this;
        int cols = this.mtx[0].length + cliEntity.mtx[0].length;
        int rows = Math.max(this.mtx.length, cliEntity.mtx.length);
        CliEntity newCliEntity = new CliEntity(rows, cols);
        for(Cell[] j : newCliEntity.mtx){
            Arrays.fill(j, new Cell(CellType.NULL, CliColors.BLACK));
        }
        for(int i = 0; i < this.mtx.length; i++){
            System.arraycopy(this.mtx[i], 0, newCliEntity.mtx[i], 0, this.mtx[0].length);
        }
        for(int i = 0; i < cliEntity.mtx.length; i++){
            System.arraycopy(cliEntity.mtx[i], 0, newCliEntity.mtx[i], this.mtx[0].length, cliEntity.mtx[0].length);
        }
        return newCliEntity;
    }

    /**
     * Stick on the bottom a CliEntity
     *
     * @param cliEntity the cli entity
     * @return the new cli entity
     */
    public CliEntity bottomStick(CliEntity cliEntity){
        if(this.isDegenerate() && !cliEntity.isDegenerate()) return cliEntity;
        if(cliEntity.isDegenerate() && !this.isDegenerate()) return this;
        if(cliEntity.isDegenerate()) return this;
        int cols = Math.max(this.mtx[0].length, cliEntity.mtx[0].length);
        int rows = this.mtx.length + cliEntity.mtx.length;
        CliEntity newCliEntity = new CliEntity(rows, cols);
        for(Cell[] j : newCliEntity.mtx){
            Arrays.fill(j, new Cell(CellType.NULL, CliColors.BLACK));
        }
        for(int i = 0; i < this.mtx.length; i++){
            System.arraycopy(this.mtx[i], 0, newCliEntity.mtx[i], 0, this.mtx[0].length);
        }
        for(int i = 0; i < cliEntity.mtx.length; i++){
            System.arraycopy(cliEntity.mtx[i], 0, newCliEntity.mtx[i + this.mtx.length], 0, cliEntity.mtx[0].length);
        }
        return newCliEntity;
    }

    /**
     * Print the matrix on the terminal
     */
    public void print(){
        for (Cell[] cells : mtx) {
            for (int j = 0; j < mtx[0].length; j++) {
                if (cells[j].type != CellType.CHAR)
                    System.out.print(cells[j].color.value + cells[j].type.value);
                else
                    System.out.print(cells[j].color.value + cells[j].attribute);
            }
            System.out.println();
        }
        System.out.println(CliColors.CLEAR.value);
    }
}
