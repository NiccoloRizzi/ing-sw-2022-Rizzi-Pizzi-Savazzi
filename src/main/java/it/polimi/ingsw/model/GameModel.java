package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TileOutOfBoundsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameModel {
    private int unusedCoins;
    private int motherNature;
    private final ArrayList<Student> bag;
    private final ArrayList<Player> players;
    protected HashMap<Colour,Player> professors;
    private final ArrayList<Isle> isles;
    private final ArrayList<Cloud> clouds;
    private final ArrayList<Character> characters;
    private ArrayList<Team> teams;
    private ArrayList<Character> activeCharacters;

    public GameModel(){
        unusedCoins = 20;
        motherNature = (new Random()).nextInt(12);
        bag = new ArrayList<Student>();
        generateBag();
        players = new ArrayList<Player>();
        isles = new ArrayList<Isle>();
        generateIsle();
        clouds = new ArrayList<Cloud>();
        characters = new ArrayList<Character>();
        activeCharacters= new ArrayList<Character>();
        professors = new HashMap<Colour,Player>();
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
            for(int i = 0; i<24; i++)
                bag.add(new Student(c));
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

    public ArrayList<Student> extractStudents(int num){
        ArrayList<Student> extractedStud = new ArrayList<>();
        Random rand = new Random();
        // EXCEPTION
        if(num >= 0 && num < bag.size()){
            for(int i = 0; i < num; i++){
                int randomPos = rand.nextInt(bag.size());
                extractedStud.add(bag.remove(randomPos));
            }
            return extractedStud;
        }
        return null;
    }
    public void moveMN(int moves)
    {
        motherNature = (motherNature+moves)%12;
    }

    public boolean checkEmptyBag(){
        return  bag.size()<=0;
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

    public Student getRandomStudent(){
        Random rand = new Random();
        return bag.get(rand.nextInt(bag.size()));
    }

    public void addStudent(ArrayList<Student> students){
        bag.addAll(students);
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
}
