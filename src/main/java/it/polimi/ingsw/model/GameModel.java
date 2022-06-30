package it.polimi.ingsw.model;

import it.polimi.ingsw.clientModels.ClientGameModel;
import it.polimi.ingsw.clientModels.ClientIsle;
import it.polimi.ingsw.clientModels.ClientModel;
import it.polimi.ingsw.exceptions.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import it.polimi.ingsw.server.Observable;
import it.polimi.ingsw.server.Observer;

/**
 * The class represents the main game board with its components
 */
public class GameModel extends Observable<ClientModel> {
    private int unusedCoins;
    private int motherNature;
    private final int numOfPlayers;
    private final HashMap<Colour, Integer> bag;
    private final ArrayList<Player> players;
    /**
     * An hashmap associating the colour of a professor and the player who owns it
     */
    protected final HashMap<Colour, Player> professors;
    private final ArrayList<Isle> isles;
    private final ArrayList<Cloud> clouds;
    private ArrayList<Team> teams;
    private final ArrayList<Character> activeCharacters;
    /**
     * The number of isles that can still be prohibited.
     */
    int prohibited;
    final boolean expertMode;

    /**
     * Instantiates a new Game model, creating all the elements of the game board.
     *
     * @param numOfPlayers The number of players in the game
     * @param expertMode   True if playing in expert mode
     */
    public GameModel(int numOfPlayers, boolean expertMode) {
        this.numOfPlayers = numOfPlayers;
        this.expertMode = expertMode;
        unusedCoins = 20;
        prohibited = 4;
        motherNature = (new Random()).nextInt(12);
        bag = new HashMap<>();
        generateBag();
        players = new ArrayList<>();
        isles = new ArrayList<>();
        generateIsle();
        clouds = new ArrayList<>();
        for (int i = 0; i < numOfPlayers; i++) {
            clouds.add(new Cloud(i));
        }

        activeCharacters = new ArrayList<>();
        professors = new HashMap<>();
        if (expertMode){
            final int[] sorted = new Random().ints(0, 12).distinct().limit(3).toArray();
            for (int i = 0; i < 3; i++) {
                CharactersEnum character = CharactersEnum.values()[sorted[i]];
                if (character == CharactersEnum.ONE_STUD_TO_ISLE || character == CharactersEnum.ONE_STUD_TO_TABLES || character == CharactersEnum.EXCHANGE_3_STUD) {
                    activeCharacters.add(new CharacterStudents(i, character));
                } else
                    activeCharacters.add(new Character(i, character));
            }
        }

        if(numOfPlayers==4){
            teams=new ArrayList<>();
            teams.add(new Team());
            teams.add(new Team());
        }
    }

    /**
     * Returns the number of professors already assigned to a player
     * @param p The player
     * @return number of professors owned by the player
     */
    public int numberOfProfessors(Player p) {
        int num = 0;
        for (Colour c : professors.keySet()) {
            num += (professors.get(c).equals(p)) ? 1 : 0;
        }
        return num;
    }

    /**
     * Generates the students bag by adding 24 (26 minus the 2 of each colour added to the isles) students of each colour.
     */
    private void generateBag() {
        for (Colour c : Colour.values()) {
            //it only generates 24 students for each colour (instead of 26)
            //because 2 students of each colour get removed at the start of the game
            bag.put(c, 24);
        }
    }

    /**
     * Creates the isles of the game
     */
    private void generateIsle() {
        for (int i = 0; i < 12; i++)
            isles.add(new Isle(i));
    }

    /**
     * Gets the index of the isle where mother nature is positioned
     *
     * @return the index of the isle where mother nature is positioned
     */
    public int getMotherNature() {
        return motherNature;
    }

    /**
     * Randomly extracts a given number of students.
     *
     * @param num The number of students to be randomly extracted
     * @return An hashmap containing the extracted students
     * @throws StudentsOutOfBoundsException when trying to extract more students than the bag contains
     */
    public HashMap<Colour, Integer> extractStudents(int num) throws StudentsOutOfBoundsException{
        HashMap<Colour, Integer> extractedStud = new HashMap<>();
        for (Colour c : Colour.values()) {
            extractedStud.put(c, 0);
        }
        if (num >= 0 && num <= getBagSize()) {
            for (int i = 0; i < num; i++) {
                Colour extracted = extractRandomStudent();

                extractedStud.put(extracted, extractedStud.get(extracted) + 1);
            }
            return extractedStud;
        }
        return null;
    }

    /**
     * Moves mother nature by the number of isles given in input
     *
     * @param moves the number of isles to move mothernature
     */
    public void moveMN(int moves) {
        motherNature = (motherNature + moves) % isles.size();
        //notifyChange();
    }

    /**
     * Gets the professors owned by players
     *
     * @return the hashMap containing the professors owned by a player
     */
    public HashMap<Colour, Player> getProfessors(){
        return professors;
    }

    /**
     * Randomly fills the clouds
     */
    public void fillClouds(){
        int nToExtract = (numOfPlayers%2==0)?3:4;
        if(getBagSize() >= nToExtract*numOfPlayers) {
            for (Cloud c : clouds) {
                try {
                    c.addStudents(extractStudents(nToExtract));
                } catch (StudentsOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Checks if the bag is empty
     *
     * @return whether the bag is empty
     */
    public boolean checkEmptyBag() {
        int size = 0;
        for (Colour c : Colour.values()) {
            size += bag.get(c);
        }
        return size <= 0;
    }

    /**
     * Gets bag size
     *
     * @return the number of students contained in bag
     */
    public int getBagSize(){
        int total=0;
        for(Colour c: Colour.values()){
            total+=getStudents(c);
        }
        return total;
    }

    /**
     * Gets teams
     *
     * @return the teams arraylist
     */
    public ArrayList<Team> getTeams(){
        return teams;
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Gets a particular player
     *
     * @param playerID The id of the player to return
     * @return The player with that id
     */
    public Player getPlayer(int playerID) {
        for (Player p : players) {
            if (p.getID() == playerID) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gets clouds.
     *
     * @return the arraylist of clouds
     */
    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    /**
     * Gets isles.
     *
     * @return the arraylist of isles
     */
    public ArrayList<Isle> getIsles() {
        return isles;
    }

    /**
     * Gets a particular isle
     *
     * @param isleID the id of the isle to get
     * @return the isle associated with the id
     * @throws TileOutOfBoundsException when given an invalid id
     */
    public Isle getIsle(int isleID) throws TileOutOfBoundsException {
        if (isleID >= 0 && isleID < isles.size()) {
            return isles.get(isleID);
        }
        throw new TileOutOfBoundsException();
    }

    /**
     * Gets cloud.
     *
     * @param cloudID the cloud id
     * @return the cloud
     * @throws TileOutOfBoundsException the tile out of bounds exception
     */
    public Cloud getCloud(int cloudID) throws TileOutOfBoundsException {
        if (cloudID >= 0 && cloudID < clouds.size()) {
            return clouds.get(cloudID);
        }
        throw new TileOutOfBoundsException();
    }


    /**
     * Gets a particular team.
     *
     * @param teamID the id of the team to recover
     * @return the team associated with that id
     */
    public Team getTeam(int teamID) {
        // FOR EXCEPTION
        if (teamID >= 0 && teamID < teams.size()) {
            return teams.get(teamID);
        }
        return null;
    }

    /**
     * Gets the number of coins that can still be given to players
     *
     * @return the number of coins that can still be given to players
     */
    public int getCoins() {
        return unusedCoins;
    }

    /**
     * Adds a given number of coins to the gameboard
     *
     * @param numCoins The number of coins to add
     * @throws NotEnoughCoinsException when the number of coins to add is incompatible with game coin limits
     */
    public void addCoins(int numCoins) throws NotEnoughCoinsException {
        if (numCoins >= 0 && numCoins + unusedCoins <= 20)
            this.unusedCoins += numCoins;
        else if (numCoins + unusedCoins > 20) {
            throw new NotEnoughCoinsException();
        }

    }

    /**
     * Removes a coin from the gameboard
     *
     * @throws NotEnoughCoinsException when the number of coins to remove is incompatible with game coin limits
     */
    public void removeCoin() throws NotEnoughCoinsException {
        if (unusedCoins > 0)
            unusedCoins--;
        else {
            throw new NotEnoughCoinsException();
        }
    }

    /**
     * Sets mother n pos.
     *
     * @param isleID the isle id
     * @throws TileOutOfBoundsException the tile out of bounds exception
     */
    public void setMotherNPos(int isleID) throws TileOutOfBoundsException {
        if (isleID <= 11 && isleID >= 0) {
            this.motherNature = isleID;
            notifyChange();
        }
        else
            throw new TileOutOfBoundsException();
    }

    /**
     * Gets The number of students in the bag of a particular colour
     *
     * @param colour The colour of the students whose number in the bag is inquired
     * @return The number of students in the bag of a particular colour
     */
    public int getStudents(Colour colour) {
        return bag.get(colour);
    }

    /**
     * Adds a player to the game board
     *
     * @param id       the id that will be assigned to the player
     * @param nickname the nickname chosen by the player
     * @throws PlayerOutOfBoundException when adding more players than the player limit chosen during match creation
     */
    public void addPlayer(int id, String nickname) throws PlayerOutOfBoundException{
        if (players.size() < numOfPlayers){
            Player p = new Player(id, nickname);
            players.add(p);
            if (numOfPlayers == 4)
                teams.get(id > 1 ? 1 : 0).addPlayer(p);
        }else{
            throw new PlayerOutOfBoundException();
        }
    }

    /**
     * Sets a particular character among one the three usable ones
     *
     * @param pos The position (between 0 and 2) of the character to set
     * @param character The particular character to set
     */
    public void setCharacter_DEBUG(int pos, CharactersEnum character){
        if (character==CharactersEnum.ONE_STUD_TO_ISLE || character==CharactersEnum.ONE_STUD_TO_TABLES || character==CharactersEnum.EXCHANGE_3_STUD)
            activeCharacters.set(pos, new CharacterStudents(pos,character));
        else
            activeCharacters.set(pos, new Character(pos,character));
    }

    /**
     * Extract a student of a particular colour from the bag
     *
     * @return The colour of the student to extract
     * @throws StudentsOutOfBoundsException When the bag is empty
     */
    public Colour extractRandomStudent() throws StudentsOutOfBoundsException{
        Random rand = new Random();
        int selected = rand.nextInt(getBagSize());

        int verify = 0;
        for(Colour c : Colour.values()){
            verify += bag.get(c);
            if(selected <= verify){
                bag.replace(c, bag.get(c) - 1);
                return c;
            }
        }
        throw new StudentsOutOfBoundsException();
    }

    /**
     * Adds students to the bag
     *
     * @param students The hashmap containing the students to add
     */
    public void addStudents(HashMap<Colour, Integer> students){
        for(Colour c: students.keySet()){
            bag.replace(c,students.get(c)+bag.get(c));
        }
    }

    /**
     * Adds a student of a particular colour to the bag
     *
     * @param student The colour of the student to be added to the bag
     */
    public void addStudent(Colour student){
        bag.replace(student, bag.get(student)+1);
    }

    /**
     * Gets the player who owns a professor, if it owned by someone (optional)
     *
     * @param colour the colour of the professor whose owner is inquired
     * @return the optional of a player owning the professor
     */
    public Optional<Player> getProfessorOwner (Colour colour)
    {
        if(professors.containsKey(colour)){
            return Optional.of(professors.get(colour));
        }
        else return Optional.empty();
    }

    /**
     * Sets the player owner of a professor
     *
     * @param colour The colour of the professor whose owner is being set
     * @param player The player that will be set as owner as the professor
     */
    public void setProfessor(Colour colour, Player player)
    {
        professors.put(colour, player);
        notifyChange();
    }

    /**
     * Gets a particular character among the active ones
     *
     * @param id the id of the character to get
     * @return the character
     */
    public Character getCharacter (int id)
    {
        return activeCharacters.get(id);
    }

    /**
     * Checks if an isle can be joint with adjacent ones and if possible joins them
     *
     * @param isle the id of the isle to check
     * @throws TileOutOfBoundsException when the index of the isle isn't valid
     */
    public void joinIsle(int isle) throws TileOutOfBoundsException
    {
        if(isle<0 || isle>=isles.size())
            throw new TileOutOfBoundsException();
        if(isles.get(isle).getTower().equals(isles.get((isle+1)%isles.size()).getTower()) && !isles.get(isle).getTower().equals(Faction.Empty)) {
            Isle temp = isles.get(isle).join(isles.get((isle + 1) % isles.size()));
            if(isle != isles.size()-1)
            {
                isles.remove((isle+1)%isles.size());
                isles.remove(isle);
                isles.add(isle,temp);
            }else{
                isles.remove(isle);
                isles.remove(0);
                isles.add(temp);
                isle --;
                motherNature = isles.size()-1;
            }
        }
        if(isles.get(isle).getTower().equals(isles.get((isle==0)?isles.size()-1:isle-1).getTower()) && !isles.get(isle).getTower().equals(Faction.Empty)) {
            Isle temp = isles.get(isle).join(isles.get((isle==0)?isles.size()-1:isle-1));
            if(isle != 0)
            {
                isles.remove(isle);
                isles.remove(isle-1);
                isles.add(isle-1,temp);
                motherNature = isle-1;
            }else{
                isles.remove(isles.size()-1);
                isles.remove(0);
                isles.add(0,temp);
                motherNature= 0;
            }
        }
        notifyChange();
    }

    /**
     * Gets the number of prohibitions still available
     *
     * @return the number of prohibitions still available
     */
    public int getProhibited()
    {
        return prohibited;
    }

    /**
     * Reduces prohibited number by one
     */
    public void useProhibited()
    {
        prohibited --;
        notifyChange();
    }

    /**
     * Adds a prohibited
     */
    public void addProhibited()
    {
        prohibited++;
    }

    /**
     * Creates the three characters usable for the current game
     */
    public void setUpCharacters()
    {
        for (int i = 0; i < 3; i++) {
            CharactersEnum character = getCharacter(i).getCard();
            if (character == CharactersEnum.ONE_STUD_TO_ISLE) {
                try {
                    ((CharacterStudents) activeCharacters.get(i)).addStudents(extractStudents(4));
                } catch (StudentsOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            if (character == CharactersEnum.ONE_STUD_TO_TABLES) {
                try {
                    ((CharacterStudents) activeCharacters.get(i)).addStudents(extractStudents(4));
                } catch (StudentsOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            if (character == CharactersEnum.EXCHANGE_3_STUD) {
                try {
                    ((CharacterStudents) activeCharacters.get(i)).addStudents(extractStudents(6));
                } catch (StudentsOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Gives a coin to a player
     *
     * @param player The player to whom to give the coin
     */
    public void giveCoin(Player player){
        try{
            removeCoin();
            getPlayer(player.getID()).addCoin();
        } catch (NotEnoughCoinsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(Observer<ClientModel> observer) {
        super.addObserver(observer);
        for(Player p: players)
        {
            p.addObserver(observer);
        }
        for(Isle i:isles){
            i.addObserver(observer);
        }
        for(Cloud c: clouds){
            c.addObserver(observer);
        }
        for(Character c: activeCharacters){
            c.addObserver(observer);
        }
    }

    /**
     * Notifies changed professors owners and isles to the observers
     */
    public void notifyChange()
    {
        HashMap<Colour,Integer> prof = new HashMap<>();
        for (Colour c: professors.keySet())
        {
            prof.put(c, professors.get(c).getID());
        }
        ArrayList<ClientIsle> tempIsles = new ArrayList<>();
        for (Isle i:isles)
        {
            tempIsles.add(i.getClientIsle());
        }
        notify(new ClientGameModel(prof,motherNature,tempIsles,prohibited));
    }

    /**
     * Notifies the full model to the observer
     */
    public void sendFullModel()
    {
        notifyChange();
        for(Cloud c: clouds){
            c.notifyChange();
        }
        for (Player p:players){
            p.notifyChange();
            p.getBoard().notifyChange();
        }
        if(expertMode) {
            for (Character c : activeCharacters) {
                c.notifyChange();
            }
        }
    }
}
