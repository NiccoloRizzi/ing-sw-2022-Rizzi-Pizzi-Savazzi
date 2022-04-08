package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameModel {
    private int unusedCoins;
    private int motherNature;
    private final HashMap<Colour, Integer> bag;
    private final ArrayList<Player> players;
    protected HashMap<Colour,Player> professors;
    private final ArrayList<Isle> isles;
    private final ArrayList<Cloud> clouds;
    private ArrayList<Team> teams;
    private final ArrayList<Character> activeCharacters;

    public GameModel(){
        unusedCoins = 20;
        motherNature = (new Random()).nextInt(12);
        bag = new HashMap<>();
        generateBag();
        players = new ArrayList<Player>();
        isles = new ArrayList<Isle>();
        generateIsle();
        clouds = new ArrayList<Cloud>();
        activeCharacters= new ArrayList<Character>();
        professors = new HashMap<Colour,Player>();
        final int[] id = new Random().ints(0, 12).distinct().limit(3).toArray();
        for (int i = 0; i < 3; i++)
        {
            if(id[i] == 0 || id[i] == 6 || id[i] == 10 )
                activeCharacters.add(new CharacterStudents(id[i],id[i]%3 +1));
            else
                activeCharacters.add(new Character(id[i],id[i]%3 +1));
        }
    }

    public int numberOfProfessors(Player p){
        int num=0;
        for(Colour c : Colour.values()){
            num+=(professors.get(c).equals(p))?1:0;
        }
        return num;
    }

    private void generateBag()
    {
        for(Colour c: Colour.values())
        {
            //it only generates 24 students for each colour (instead of 26)
            //because 2 students of each colour get removed at the start of the game
            bag.put(c, 24);
        }
    }

    private void generateIsle()
    {
        for(int i = 0; i<12; i++)
            isles.add(new Isle());
    }

    public int getMotherNature() {
        return motherNature;
    }

    public HashMap<Colour, Integer> extractStudents(int num){
        HashMap<Colour, Integer> extractedStud = new HashMap<>();
        for(Colour c : Colour.values()){
            extractedStud.put(c, 0);
        }
        if(num >= 0 && num < bag.size()){
            for(int i = 0; i < num; i++){
                Colour extracted = getRandomStudent();
                extractedStud.put(extracted, extractedStud.get(extracted) + 1);
            }
            return extractedStud;
        }
        return null;
//        HashMap<Colour, Integer> extractedStud = new HashMap<>();
//        Random rand = new Random();
//        // EXCEPTION
//        if(num >= 0 && num < bag.size()){
//            for(int i = 0; i < num; i++){
//                int randomPos = rand.nextInt(bag.size());
//                extractedStud.add(bag.remove(randomPos));
//            }
//            return extractedStud;
//        }
//        return null;
    }

    public void moveMN(int moves)
    {
        motherNature = (motherNature+moves)%12;
    }

    public boolean checkEmptyBag(){
        int size = 0;
        for(Colour c : Colour.values()){
            size += bag.get(c);
        }
        return size<=0;
    }

    public ArrayList<Player> getPlayers(){
        return  players;
    }

    public Player getPlayer(int playerID) {
        for(Player p : players){
            if(p.getID() == playerID){
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

    public Isle getIsle(int isleID){
        // FOR EXCEPTION
        if(isleID >= 0 && isleID < isles.size()){
            return isles.get(isleID);
        }
        return null;
    }

    public Cloud getCloud(int cloudID){
        // FOR EXCEPTION
        if(cloudID >= 0 && cloudID < clouds.size()){
            return clouds.get(cloudID);
        }
        return null;
    }

    public Team getTeam(int teamID){
        // FOR EXCEPTION
        if(teamID >= 0 && teamID < teams.size()){
            return teams.get(teamID);
        }
        return null;
    }

    public int getCoins(){
        return unusedCoins;
    }

    public void addCoins(int numCoins){
        if(numCoins>=0)
            this.unusedCoins += numCoins;
    }

    public void removeCoin() {
        if(unusedCoins>0)
            unusedCoins--;
    }

    public void setMotherNPos (int isleID) throws TileOutOfBoundsException{
        if(isleID<=11 && isleID>=0)
            this.motherNature = isleID;
        else
            throw new TileOutOfBoundsException();
    }

    public Colour getRandomStudent(){
        int totStud = 0;
        for(Colour c : bag.keySet()){
            totStud += bag.get(c);
        }
        Random rand = new Random();
        int selected = rand.nextInt(totStud);
        Colour selectedColour = null;

        int verify = 0;
        for(Colour c : Colour.values()){
            verify += bag.get(c);
            if(selected <= verify){
                selectedColour = c;
            }
        }

        bag.replace(selectedColour, bag.get(selectedColour) - 1);
        return selectedColour;
//        Random rand = new Random();
//        return bag.get(rand.nextInt(bag.size()));
    }

    public void addStudent(HashMap<Colour, Integer> students){
        bag.forEach((c,v)->v += students.get(c));
    }

    public Player getProfessor (Colour c)
    {
        return professors.get(c);
    }

    public void setProfessor(Colour c, Player p)
    {
        // professors.replace(c,p);
        professors.put(c, p);
    }

    public Character getCharacter (int id)
    {
        return activeCharacters.get(id);
    }

    public void joinIsle(int isle1,int isle2)
    {
        AggregatedIsland temp =isles.get(isle1).join(isles.get(isle2));
        if(isle1>isle2)
        {
            isles.remove(isle1);
            isles.remove(isle2);
            isles.add(isle2,temp);
        }
        else
        {
            isles.remove(isle2);
            isles.remove(isle1);
            isles.add(isle1,temp);
        }
    }
}
