package it.polimi.ingsw.client;

import it.polimi.ingsw.clientModels.*;
import it.polimi.ingsw.clientModels.Answers.StartMessage;
import it.polimi.ingsw.model.CharactersEnum;
import it.polimi.ingsw.model.Colour;
import it.polimi.ingsw.model.Faction;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CliUtil {

    public static class Cell{
        private final CellType type;
        private final CliColors color;
        private final String attribute;

        public Cell(CellType type, CliColors color){
            this.type = type;
            this.color = color;
            attribute = null;
        }

        public Cell(CellType type, CliColors color, String attribute){
            this.type = type;
            this.color = color;
            this.attribute = attribute;
        }
    }

    private enum CliColors{
        RED("\u001b[31m"),
        GREEN("\u001b[32m"),
        PINK("\u001b[35m"),
        GREY("\u001b[37m"),
        BLACK("\u001b[30m"),
        WHITE("\u001b[97m"),
        YELLOW("\u001b[93m"),
        BLUE("\u001b[34m"),
        BROWN("\u001b[33m");

        private final String value;

        CliColors(String value){
            this.value = value;
        }
    }

    private enum CellType{
        NULL(" " + " " + " "),
        BORDER_V(" " + "\u2551" + " "),
        BORDER_O("\u2550" + "\u2550" + "\u2550"),
        BORDER_NE("\u2550" + "\u2557" + " "),
        BORDER_SE("\u2550" + "\u255d" + " "),
        BORDER_NO(" " + "\u2554" + "\u2550"),
        BORDER_SO(" " + "\u255a" + "\u2550"),
        BORDER_3D(" " + "\u2560" + "\u2550"),
        BORDER_3S("\u2550" + "\u2563" + " "),
        STUDENT(" " + "\u25cf" + " "),
        PROF(" " + "\u25a0" + " "),
        TOWER(" " + "\u25b2" + " "),
        CHAR(" " +" " +" "),
        PRHOIBITED(" " + "#" + " "),
        MN(" " + "\u25e6" + " ");

        private final String value;

        CellType(String value) {
            this.value = value;
        }
    }

    private final ModelView modelView;

    public CliUtil(ModelView modelView) throws IOException {
        this.modelView = modelView;
    }

    // Move the player board on the first position of the array
    public void initBoardDisposition(){
        ClientBoard[] boards = modelView.getBoards();
        ClientPlayer[] players = modelView.getPlayers();
        ClientBoard temp = boards[0];
        ClientPlayer Ptemp = players[0];
        boards[0] = boards[modelView.getMyID()];
        players[0] = players[modelView.getMyID()];
        boards[modelView.getMyID()] = temp;
        players[modelView.getMyID()] = Ptemp;
    }

    private Cell getStudCellType(Colour c) {
        switch (c){
            case Dragons: return new Cell(CellType.STUDENT, CliColors.RED);
            case Gnomes: return new Cell(CellType.STUDENT, CliColors.YELLOW);
            case Fairies: return new Cell(CellType.STUDENT, CliColors.PINK);
            case Frogs: return new Cell(CellType.STUDENT, CliColors.GREEN);
            case Unicorns: return new Cell(CellType.STUDENT, CliColors.BLUE);
            default: return null;
        }
    }
    private Cell getProfCellType(Colour c) {
        switch (c){
            case Dragons: return new Cell(CellType.PROF, CliColors.RED);
            case Gnomes: return new Cell(CellType.PROF, CliColors.YELLOW);
            case Fairies: return new Cell(CellType.PROF, CliColors.PINK);
            case Frogs: return new Cell(CellType.PROF, CliColors.GREEN);
            case Unicorns: return new Cell(CellType.PROF, CliColors.BLUE);
            default: return null;
        }
    }

    public static void printCells(Cell[][] mtx){
        for (int i = 0; i < mtx.length; i++) {
            for (int j = 0; j < mtx[0].length; j++) {
                if(mtx[i][j].type != CellType.CHAR)
                    System.out.print(mtx[i][j].color.value  + mtx[i][j].type.value);
                else
                    System.out.print(mtx[i][j].color.value + mtx[i][j].attribute);
            }
            System.out.println();
        }
    }

    public Cell[][] unifyMtxSD(Cell[][] mtxA, Cell[][] mtxB){
        if(mtxA.length == 0) return mtxB;
        if(mtxB.length == 0) return mtxA;
        int cols = mtxA[0].length + mtxB[0].length;
        int rows = Math.max(mtxA.length, mtxB.length);
        Cell[][] mtx = new Cell[rows][cols];
        for(Cell[] j : mtx){
            Arrays.fill(j, new Cell(CellType.NULL, CliColors.BLACK));
        }
        for(int i = 0; i < mtxA.length; i++){
            System.arraycopy(mtxA[i], 0, mtx[i], 0, mtxA[0].length);
        }
        for(int i = 0; i < mtxB.length; i++){
            System.arraycopy(mtxB[i], 0, mtx[i], mtxA[0].length, mtxB[0].length);
        }
        return mtx;
    }
    public Cell[][] unifyMtxNS(Cell[][] mtxA, Cell[][] mtxB){
        if(mtxA.length == 0) return mtxB;
        if(mtxB.length == 0) return mtxA;
        int cols = Math.max(mtxA[0].length, mtxB[0].length);
        int rows = mtxA.length + mtxB.length;
        Cell[][] mtx = new Cell[rows][cols];
        for(Cell[] j : mtx){
            Arrays.fill(j, new Cell(CellType.NULL, CliColors.BLACK));
        }
        for(int i = 0; i < mtxA.length; i++){
            System.arraycopy(mtxA[i], 0, mtx[i], 0, mtxA[0].length);
        }
        for(int i = 0; i < mtxB.length; i++){
            System.arraycopy(mtxB[i], 0, mtx[i + mtxA.length], 0, mtxB[0].length);
        }
        return mtx;
    }

    private void addStringToMtx(Cell[][] mtx, int i, int j, String string){
        for(int c = 0; c < string.length(); c += 3){
            if(c + 2 < string.length())
                mtx[i][j + c / 3] = new Cell(CellType.CHAR, CliColors.WHITE, "" + string.charAt(c) + string.charAt(c + 1) + string.charAt(c + 2));
            else if (c + 1 < string.length())
                mtx[i][j + c / 3] = new Cell(CellType.CHAR, CliColors.WHITE, "" + string.charAt(c) + string.charAt(c + 1) + " ");
            else
                mtx[i][j + c / 3] = new Cell(CellType.CHAR, CliColors.WHITE, "" + string.charAt(c) + "  ");
        }
    }
    public Cell[][] generateBoard(int ID){
        // Init
        Cell[][] mtx = new Cell[21][7];
        for (Cell[] chars : mtx) {
            Arrays.fill(chars, new Cell(CellType.NULL, CliColors.BLACK));
        }
        ClientBoard board = modelView.getBoards()[ID];
        // NickName
        addStringToMtx(mtx, 0, 1, modelView.getPlayers()[board.getPlayerID()].getNickname());

        // Set entrance
        int row = 2, col = 1;
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
                row = 14 - i;
                mtx[row][col] = getStudCellType(c);
            }
        }
        // Set professor
        row = 16;
        for(Colour c : modelView.getGameModel().getProfessors().keySet()){
            if(modelView.getGameModel().getProfessors().get(c) == modelView.getMyID()){
                col = 1 + c.ordinal();
                mtx[row][col] = getProfCellType(c);
            }
        }
        // Set towers
        row = 18;
        col = 1;
        for(int i = 0; i < board.getTowers(); i++){
            switch (board.getFaction()){
                case Black: mtx[row][col] = new Cell(CellType.TOWER, CliColors.BLACK); break;
                case White: mtx[row][col] = new Cell(CellType.TOWER, CliColors.WHITE); break;
                case Grey: mtx[row][col] = new Cell(CellType.TOWER, CliColors.GREY); break;
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
            mtx[1][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
            mtx[mtx.length - 1][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
            mtx[4][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
            mtx[15][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
            mtx[17][i] = new Cell(CellType.BORDER_O, CliColors.GREY);
        }
        // Vertical
        for(int i = 1; i < mtx.length; i++){
            mtx[i][0] = new Cell(CellType.BORDER_V, CliColors.GREY);
            mtx[i][mtx[0].length - 1] = new Cell(CellType.BORDER_V, CliColors.GREY);
        }
        // Corners
        mtx[1][0] = new Cell(CellType.BORDER_NO, CliColors.GREY);
        mtx[1][mtx[0].length - 1] = new Cell(CellType.BORDER_NE, CliColors.GREY);
        mtx[mtx.length - 1][0] = new Cell(CellType.BORDER_SO, CliColors.GREY);
        mtx[mtx.length - 1][mtx[0].length - 1] = new Cell(CellType.BORDER_SE, CliColors.GREY);
        // 3 Ways
        mtx[4][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
        mtx[4][mtx[0].length - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
        mtx[15][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
        mtx[15][mtx[0].length - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
        mtx[17][0] = new Cell(CellType.BORDER_3D, CliColors.GREY);
        mtx[17][mtx[0].length - 1] = new Cell(CellType.BORDER_3S, CliColors.GREY);
        return mtx;
    }
    public Cell[][] generateBoards(){
        Cell[][] mtx = new Cell[0][0];
        for(ClientBoard board : modelView.getBoards()){
            mtx = unifyMtxSD(mtx, generateBoard(board.getPlayerID()));
        }
        return mtx;
    }
    public Cell[][] generateIsle(int ID){
        ClientIsle isle = modelView.getGameModel().getIsles().get(ID);
        Faction faction = isle.getControlling();
        HashMap<Colour, Integer> students = isle.getStudents();
        int prohibited = isle.getProhibited();
        int size = isle.getSize();
        final int ROWS = 13;
        final int COLS = 8;
        Cell[][] mtx = new Cell[ROWS][COLS];
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
        addStringToMtx(mtx, 1, 1, "Isola: " + (ID + 1));
        // Towers
        switch (faction){
            case Grey:
                mtx[2][1] = new Cell(CellType.TOWER, CliColors.GREY);
                addStringToMtx(mtx, 2, 2, "" + size);
                break;
            case White: mtx[2][1] = new Cell(CellType.TOWER, CliColors.WHITE);
                addStringToMtx(mtx, 2, 2, "" + size);
                break;
            case Black: mtx[2][1] = new Cell(CellType.TOWER, CliColors.BLACK);
                addStringToMtx(mtx, 2, 2, "" + size);
                break;
            default: mtx[2][1] = new Cell(CellType.NULL, CliColors.BLACK);
        }
        // Students
        for(Colour c : Colour.values()){
            mtx[c.ordinal() + 4][1] = getStudCellType(c);
            addStringToMtx(mtx, c.ordinal() + 4, 2, "" + ((students.get(c) != null)?students.get(c):"0"));
        }
        // Prohibited
        mtx[10][1] = new Cell(CellType.PRHOIBITED, CliColors.RED);
        addStringToMtx(mtx, 10, 2, "" + prohibited);
        // Mn
        if(modelView.getGameModel().getMotherNature() == isle.getId()){
            mtx[11][1] = new Cell(CellType.MN, CliColors.BROWN);
            addStringToMtx(mtx, 11, 2, "Mother Nature");
        }
        return mtx;
    }
    public Cell[][] generateIsles(){
        ArrayList<ClientIsle> isles = modelView.getGameModel().getIsles();
        Cell[][] tempMtx = new Cell[0][0];
        Cell[][] mtx = new Cell[0][0];
        for(int i = 0; i < isles.size(); i++){
            tempMtx = unifyMtxSD(tempMtx, generateIsle(i));
            if((i + 1) % 4 == 0){
                mtx = unifyMtxNS(mtx, tempMtx);
                tempMtx = new Cell[0][0];
            }
        }
        return mtx;
    }
    public Cell[][] generateCharacter(int ID){
        ClientCharacter character = modelView.getCharacters()[ID];
        int id = character.getID();
        int price = character.getPrice();
        HashMap<Colour, Integer> students = character.getStudents();
        final int ROWS = 9;
        final int COLS = 9;
        Cell[][] mtx = new Cell[ROWS][COLS];
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
        // Character id
        addStringToMtx(mtx, 1, 1, "" + character.getCard());
        addStringToMtx(mtx, 2, 1, "Price " + price);
        // Students
        if(students != null){
            for(Colour c : Colour.values()){
                mtx[c.ordinal() + 3][1] = getStudCellType(c);
                addStringToMtx(mtx, c.ordinal() + 3, 2, "" + students.get(c));
            }
        }else{
            addStringToMtx(mtx, 3, 1, "No students");
        }
        return mtx;
    }
    public Cell[][] generateCharacters(){
        Cell[][] mtx = new Cell[0][0];
        for(ClientCharacter character : modelView.getCharacters()){
            mtx = unifyMtxSD(mtx, generateCharacter(character.getID()));
        }
        return mtx;
    }
    public Cell[][] generateCloud(int ID){
        ClientCloud cloud = modelView.getClouds()[ID];
        int id = cloud.getId();
        HashMap<Colour, Integer> students = cloud.getStudents();
        final int ROWS = 8;
        final int COLS = 5;
        Cell[][] mtx = new Cell[ROWS][COLS];
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
        addStringToMtx(mtx, 1, 1, "Cloud " + (id + 1));
        // Students
        if(students != null){
            for(Colour c : Colour.values()){
                mtx[c.ordinal() + 2][1] = getStudCellType(c);
                addStringToMtx(mtx, c.ordinal() + 2, 2, "" + students.get(c));
            }
        }
        return mtx;
    }
    public Cell[][] generateClouds(){
        Cell[][] mtx = new Cell[0][0];
        for(ClientCloud cloud : modelView.getClouds()){
            mtx = unifyMtxSD(mtx, generateCloud(cloud.getId()));
        }
        return mtx;
    }
    public Cell[][] generateAssistant(int ID, boolean isDeck, int playerID){
        Integer assistant = null;
        if(isDeck){
            assistant = modelView.getPlayers()[playerID].getDeck()[ID];
        }else{
            assistant = modelView.getPlayers()[playerID].getUsedAssistants()[ID];
        }
        final int ROWS = 4;
        final int COLS = 5;
        Cell[][] mtx = new Cell[ROWS][COLS];
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
        // Assistant info
        addStringToMtx(mtx, 1, 1, "Value: " + (assistant));
        addStringToMtx(mtx, 2, 1, "Mn: " + ((assistant+1)/2));
        return mtx;
    }
    public Cell[][] generateDeck(int playerID){
        Cell[][] mtx = new Cell[0][0];
        for(int i = 0; i < modelView.getPlayers()[playerID].getDeck().length; i++){
            mtx = unifyMtxSD(mtx, generateAssistant(i, true, playerID));
        }
        return mtx;
    }
    public Cell[][] generateUsedAssistants(int playerID){
        Cell[][] mtx = new Cell[0][0];
        for(int i = 0; i < modelView.getPlayers()[playerID].getUsedAssistants().length; i++){
            mtx = unifyMtxSD(mtx, generateAssistant(i, false, playerID));
        }
        return mtx;
    }
    public Cell[][] generateStringMtx(String string){
        Cell[][] mtx = new Cell[1][string.length() / 3];
        addStringToMtx(mtx, 0,0, string);
        return mtx;
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(true);
        View view = new View(client);
        HashMap<Colour, Integer> entrance = new HashMap<>();
        entrance.put(Colour.Dragons, 3);
        entrance.put(Colour.Gnomes, 4);
        entrance.put(Colour.Unicorns, 1);
        entrance.put(Colour.Frogs, 2);
        HashMap<Colour, Integer> tables = new HashMap<>();
        tables.put(Colour.Fairies, 5);
        tables.put(Colour.Frogs, 1);
        ClientBoard clientBoard = new ClientBoard(0, Faction.Black, 8, tables, entrance);
        ClientBoard clientBoard_2 = new ClientBoard(1, Faction.Black, 8, tables, entrance);
        HashMap<Colour, Integer> professors = new HashMap<>();
        professors.put(Colour.Frogs, 0);
        professors.put(Colour.Fairies, 0);
        ClientGameModel clientGameModel = new ClientGameModel(professors, 0, new ArrayList<>(12));
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(1, "Federico"));
        players.add(new Player(0, "Giacomo"));
        players.get(0).createBoard(8);
        players.get(1).createBoard(8);
        view.setNickname("Federico");
        view.visit(new StartMessage(players));
        Integer[] a = {1, 3, 5};
        Integer[] b = {1, 7};
        Integer[] c = {5, 1};
        Integer[] d = {1, 3, 9};
        view.visit(new ClientPlayer(a, b, 5, "Federico", 1));
        view.visit(new ClientPlayer(c, d, 0, "Giacomo", 0));
        view.visit(clientBoard);
        view.visit(clientBoard_2);
        view.visit(clientGameModel);
        view.visit(new ClientCharacter(0, CharactersEnum.ONE_STUD_TO_ISLE, 1, entrance));
        view.visit(new ClientCharacter(1, CharactersEnum.REMOVE_3_STUD, 2));
        view.visit(new ClientCharacter(2, CharactersEnum.PROHIBITED, 3));
        view.getGameModel().getIsles().add(new ClientIsle(0, Faction.Black, entrance, 1, 1));
        view.getGameModel().getIsles().add(new ClientIsle(1, Faction.White, tables, 0, 1));
        view.getGameModel().getIsles().add(new ClientIsle(2, Faction.Black, entrance, 1, 1));
        view.getGameModel().getIsles().add(new ClientIsle(3, Faction.White, entrance, 10, 1));
        view.getGameModel().getIsles().add(new ClientIsle(4, Faction.Black, tables, 1, 1));
        view.getGameModel().getIsles().add(new ClientIsle(5, Faction.Empty, entrance, 1, 1));
        view.getGameModel().getIsles().add(new ClientIsle(6, Faction.Black, tables, 0, 1));
        view.getGameModel().getIsles().add(new ClientIsle(7, Faction.Empty, entrance, 1, 1));
        view.getGameModel().getIsles().add(new ClientIsle(8, Faction.Black, tables, 4, 1));
        view.getGameModel().getIsles().add(new ClientIsle(9, Faction.Grey, entrance, 1, 1));
        view.getGameModel().getIsles().add(new ClientIsle(10, Faction.Black, tables, 3, 5));
        view.getGameModel().getIsles().add(new ClientIsle(11, Faction.Grey, entrance, 1, 1));
        HashMap<Colour, Integer> cloudStuds = new HashMap<>();
        cloudStuds.put(Colour.Dragons, 2);
        cloudStuds.put(Colour.Gnomes, 1);
        cloudStuds.put(Colour.Unicorns, 1);
        view.visit(new ClientCloud(0, cloudStuds));
        view.visit(new ClientCloud(1, cloudStuds));
        CliUtil cli = new CliUtil(view);
//        cli.initBoardDisposition();
        Cell[][] islesMtx = cli.generateIsles();
        Cell[][] boardsMtx = cli.generateBoards();
        Cell[][] charactersMtx = cli.generateCharacters();
        Cell[][] cloudsMtx = cli.generateClouds();
        Cell[][] deckMtx = cli.generateDeck(view.getMyID());
        Cell[][] usedAssistantMtx = cli.generateUsedAssistants(view.getMyID());
        Cell[][] assistantsMtx = cli.unifyMtxNS(deckMtx, usedAssistantMtx);
        Cell[][] boardPLusChar = cli.unifyMtxNS(boardsMtx, charactersMtx);
        Cell[][] boardCharCloudMtx = cli.unifyMtxNS(boardPLusChar, cloudsMtx);
        Cell[][] boardCharCloudAssMtx = cli.unifyMtxNS(boardCharCloudMtx, assistantsMtx);
        Cell[][] game = cli.unifyMtxSD(boardCharCloudAssMtx, islesMtx);
        System.out.println("My id " + view.getMyID());
        CliUtil.printCells(game);
    }
}
