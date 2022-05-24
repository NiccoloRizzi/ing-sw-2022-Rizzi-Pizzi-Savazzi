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

public class GameModel extends Observable<ClientModel> {
    private int unusedCoins;
    private int motherNature;
    private final int numOfPlayers;
    private final HashMap<Colour, Integer> bag;
    private final ArrayList<Player> players;
    protected HashMap<Colour, Player> professors;
    private final ArrayList<Isle> isles;
    private final ArrayList<Cloud> clouds;
    private ArrayList<Team> teams;
    private final ArrayList<Character> activeCharacters;
    int prohibited;
    boolean expertMode;
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
            activeCharacters.set(0, new Character(0, CharactersEnum.PROHIBITED));
        }

        if(numOfPlayers==4){
            teams=new ArrayList<>();
            teams.add(new Team());
            teams.add(new Team());
        }
    }

    public int numberOfProfessors(Player p) {
        int num = 0;
        for (Colour c : professors.keySet()) {
            num += (professors.get(c).equals(p)) ? 1 : 0;
        }
        return num;
    }

    private void generateBag() {
        for (Colour c : Colour.values()) {
            //it only generates 24 students for each colour (instead of 26)
            //because 2 students of each colour get removed at the start of the game
            bag.put(c, 24);
        }
    }

    private void generateIsle() {
        for (int i = 0; i < 12; i++)
            isles.add(new Isle(i));
    }

    public int getMotherNature() {
        return motherNature;
    }

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

    public void moveMN(int moves) {
        motherNature = (motherNature + moves) % isles.size();
        //notifyChange();
    }

    public HashMap<Colour, Player> getProfessors(){
        return professors;
    }

    public boolean checkEmptyBag() {
        int size = 0;
        for (Colour c : Colour.values()) {
            size += bag.get(c);
        }
        return size <= 0;
    }

    public int getBagSize(){
        int total=0;
        for(Colour c: Colour.values()){
            total+=getStudents(c);
        }
        return total;
    }

    public ArrayList<Team> getTeams(){
        return teams;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int playerID) {
        for (Player p : players) {
            if (p.getID() == playerID) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Cloud> getClouds() {
        return clouds;
    }

    public ArrayList<Isle> getIsles() {
        return isles;
    }

    public Isle getIsle(int isleID) throws TileOutOfBoundsException {
        if (isleID >= 0 && isleID < isles.size()) {
            return isles.get(isleID);
        }
        throw new TileOutOfBoundsException();
    }

    public Cloud getCloud(int cloudID) throws TileOutOfBoundsException {
        if (cloudID >= 0 && cloudID < clouds.size()) {
            return clouds.get(cloudID);
        }
        throw new TileOutOfBoundsException();
    }


    public Team getTeam(int teamID) {
        // FOR EXCEPTION
        if (teamID >= 0 && teamID < teams.size()) {
            return teams.get(teamID);
        }
        return null;
    }

    public int getCoins() {
        return unusedCoins;
    }

    public void addCoins(int numCoins) throws NotEnoughCoinsException {
        if (numCoins >= 0 && numCoins + unusedCoins <= 20)
            this.unusedCoins += numCoins;
        else if (numCoins + unusedCoins > 20) {
            throw new NotEnoughCoinsException();
        }

    }

    public void removeCoin() throws NotEnoughCoinsException {
        if (unusedCoins > 0)
            unusedCoins--;
        else {
            throw new NotEnoughCoinsException();
        }
    }

    public void setMotherNPos(int isleID) throws TileOutOfBoundsException {
        if (isleID <= 11 && isleID >= 0) {
            this.motherNature = isleID;
            notifyChange();
        }
        else
            throw new TileOutOfBoundsException();
    }

    public int getStudents(Colour c) {
        return bag.get(c);
    }

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

    public void setCharacter_DEBUG(int pos, CharactersEnum character){
        if (character==CharactersEnum.ONE_STUD_TO_ISLE || character==CharactersEnum.ONE_STUD_TO_TABLES || character==CharactersEnum.EXCHANGE_3_STUD)
            activeCharacters.set(pos, new CharacterStudents(pos,character));
        else
            activeCharacters.set(pos, new Character(pos,character));
    }

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

    public void addStudents(HashMap<Colour, Integer> students){
        for(Colour c: students.keySet()){
            bag.replace(c,students.get(c)+bag.get(c));
        }
    }

    public void addStudent(Colour student){
        bag.replace(student, bag.get(student)+1);
    }

    public Optional<Player> getProfessorOwner (Colour c)
    {
        if(professors.containsKey(c)){
            return Optional.of(professors.get(c));
        }
        else return Optional.empty();
    }

    public void setProfessor(Colour c, Player p)
    {
        professors.put(c, p);
        notifyChange();
    }

    public Character getCharacter (int id)
    {
        return activeCharacters.get(id);
    }

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
                motherNature = isles.size()-1;
            }else{
                isles.remove(isles.size()-1);
                isles.remove(0);
                isles.add(0,temp);
                motherNature= 0;
            }
        }
        notifyChange();
    }

    public int getProhibited()
    {
        return prohibited;
    }
    public void useProhibited()
    {
        prohibited --;
        notifyChange();
    }

    public void addProhibited()
    {
        prohibited++;
    }

    public void setUpCharacter()
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

    public void giveCoin(Player p){
        String answer = null;
        try{
            removeCoin();
            getPlayer(p.getID()).addCoin();
        } catch (NotEnoughCoinsException e) {
            e.printStackTrace();
            answer = "Available coins finished";
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
